package raft

import "sync"
import "sync/atomic"
import "../labrpc"
import "time"
import "math/rand"
import "fmt"
import "bytes"
import "../labgob"


type ApplyMsg struct {
	CommandValid bool
	Command      interface{}
	CommandIndex int
}

type Entry struct {
	Command interface{}
	Term int
}
//
// A Go object implementing a single Raft peer.
//
type Raft struct {
	mu        sync.Mutex          // Lock to protect shared access to this peer's state
	peers     []*labrpc.ClientEnd // RPC end points of all peers
	persister *Persister          // Object to hold this peer's persisted state
	me        int                 // this peer's index into peers[]
	dead      int32               // set by Kill()
	ch 		  chan ApplyMsg
	//(2A)
	currentTerm      int
	state 	  int  //0 follower, 1 leader, 2 candidate
	voteFor   int

	startTime time.Time
	voteNum int
	serverNum int
	
	entries []Entry
	commitIndex int
	lastApplied int
	nextIndex []int
	matchIndex []int

}

func (rf *Raft) GetState() (int, bool) {

	var term int
	var isleader bool
	term = rf.currentTerm
	isleader = (rf.state == 1)
	return term, isleader
}


func (rf *Raft) persist() {
	w := new(bytes.Buffer)
	e := labgob.NewEncoder(w)
	e.Encode(rf.currentTerm)
	e.Encode(rf.voteFor)
	for i := range rf.entries {
		e.Encode(rf.entries[i])
	}
	data := w.Bytes()
	rf.persister.SaveRaftState(data)
}


func (rf *Raft) readPersist(data []byte) {
	if data == nil || len(data) < 1 { // bootstrap without any state?
		return
	}

	r := bytes.NewBuffer(data)
	d := labgob.NewDecoder(r)
	var currentTerm int
	var voteFor int 
	var entry Entry
	entries := []Entry{}
	if d.Decode(&currentTerm) != nil ||
	   d.Decode(&voteFor) != nil {
	  fmt.Println("Error")
	} else {
	  for {
		  if d.Decode(&entry) != nil {
			  break
		  } else {
			  entries = append(entries, entry)
		  }
	  }
	  rf.currentTerm = currentTerm
	  rf.voteFor = voteFor
	  rf.entries = entries
	}
}



type RequestVoteArgs struct {
	// 2A
	Term int
	CandidateId int
	LastLogIndex int
	LastLogTerm int

}


type RequestVoteReply struct {
	// Your data here (2A).
	Term int
	VoteGranted bool
}


func (rf *Raft) RequestVote(args *RequestVoteArgs, reply *RequestVoteReply) {

	rf.mu.Lock()
	defer rf.mu.Unlock()
	if rf.killed() {
		return
	}
	if args.Term > rf.currentTerm {
		rf.turnToFollower(args.Term)
	}
	if args.Term == rf.currentTerm && (rf.voteFor ==-1 || rf.voteFor == args.CandidateId) && ((args.LastLogTerm > rf.entries[len(rf.entries)-1].Term) || (args.LastLogTerm == rf.entries[len(rf.entries)-1].Term && args.LastLogIndex >= (len(rf.entries)-1))) {
		rf.voteFor = args.CandidateId
		
		reply.VoteGranted = true
		rf.startTime = time.Now()
	} else{
		reply.VoteGranted = false
	}
	reply.Term = rf.currentTerm
	rf.persist()
}


func (rf *Raft) Start(command interface{}) (int, int, bool) {
	rf.mu.Lock()
	defer rf.mu.Unlock()

	index := len(rf.entries)
	term := rf.currentTerm
	isLeader := rf.state == 1
	
	if isLeader {
		entry := Entry{Command: command, Term: term}
		rf.entries = append(rf.entries, entry)
		rf.persist()
	}

	return index, term, isLeader
}


func (rf *Raft) Kill() {
	atomic.StoreInt32(&rf.dead, 1)
	// Your code here, if desired.
}

func (rf *Raft) killed() bool {
	z := atomic.LoadInt32(&rf.dead)
	return z == 1
}

func Make(peers []*labrpc.ClientEnd, me int,
	persister *Persister, applyCh chan ApplyMsg) *Raft {
	rf := &Raft{}
	rf.peers = peers
	rf.persister = persister
	rf.me = me
	rf.ch = applyCh

	// Your initialization code here (2A, 2B, 2C).
	rf.serverNum = len(rf.peers)
	rf.commitIndex = 0
	rf.entries = []Entry{Entry{Command:1, Term:0}}

	rf.state = 0
	rf.currentTerm = 0
	rf.startTime = time.Now()
	rf.voteFor = -1
	rf.voteNum = 0
	rf.lastApplied = 0
	// initialize from state persisted before a crash
	rf.readPersist(persister.ReadRaftState())

	//go rf.candidateCheck()
	go rf.runElectionTimer()

	return rf
}

func (rf *Raft) runElectionTimer() {
	timeoutDuration := rand.Int63n(800)+800
	rf.mu.Lock()
	termStarted := rf.currentTerm
	rf.mu.Unlock()

	ticker := time.NewTicker(10 * time.Millisecond)
	defer ticker.Stop()
	for {
		<-ticker.C

		rf.mu.Lock()
		if rf.state != 2 && rf.state != 0 {
			rf.mu.Unlock()
			return
		}

		if termStarted != rf.currentTerm {
			rf.mu.Unlock()
			return
		}

		if elapsed := time.Since(rf.startTime); elapsed.Milliseconds() >= timeoutDuration {
			rf.startElection()
			rf.mu.Unlock()
			return
		}
		rf.mu.Unlock()
	}
}

func (rf *Raft) startElection() {
	rf.state = 2
	rf.currentTerm += 1
	savedCurrentTerm := rf.currentTerm
	rf.startTime = time.Now()
	rf.voteFor = rf.me

	votesReceived := 0

	for  peerId, _ := range rf.peers {
		go func(peerId int) {

			args := RequestVoteArgs{
				Term:         savedCurrentTerm,
				CandidateId:  rf.me,
				LastLogIndex: len(rf.entries)-1,
				LastLogTerm:  rf.entries[len(rf.entries)-1].Term,
			}

			var reply RequestVoteReply
			if ok := rf.peers[peerId].Call( "Raft.RequestVote", &args, &reply); ok == true {
				rf.mu.Lock()
				defer rf.mu.Unlock()

				if rf.state != 2 {
					return
				}

				if reply.Term > savedCurrentTerm {
					rf.turnToFollower(reply.Term)
					return
				} else if reply.Term == savedCurrentTerm {
					if reply.VoteGranted {
						votesReceived += 1
						if votesReceived > len(rf.peers)/2 {
							rf.state=1
							go rf.heartbeat()
							return
						}
					}
				}
			}
		}(peerId)
	}

	// Run another election timer, in case this election is not successful.
	go rf.runElectionTimer()
}

type HeartbeatArgs struct {
	Term int
	LeaderId int
	PrevLogIndex int
	PrevLogTerm int
	Entries	[]Entry
	LeaderCommit int 
}


type HeartbeatReply struct {
	Term int
	Success bool
}


func (rf *Raft) heartbeat() {

	//heartbeatTimeout := 100
	rf.nextIndex = make([]int, rf.serverNum)
	rf.matchIndex = make([]int, rf.serverNum)
	for i:=0; i<rf.serverNum; i++ {
		rf.nextIndex[i] = len(rf.entries)-1
		rf.matchIndex[i] = 0
	}
	go func(){
		ticker:=time.NewTicker(100*time.Millisecond)
		defer ticker.Stop()
		for {
			for i, _ := range rf.peers {
				if (i != rf.me) {
					args := HeartbeatArgs{}
					reply := HeartbeatReply{}
					
					go rf.sendHeartbeat(i, &args, &reply)
				}
			}
			<-ticker.C

			rf.mu.Lock()
			if rf.state!=1 || rf.killed(){
				rf.mu.Unlock()
				return
			}
			rf.mu.Unlock()
		}
	}()

}


func (rf *Raft) sendHeartbeat(server int, args *HeartbeatArgs, reply *HeartbeatReply) bool {
	rf.mu.Lock()
	savedCurrentTerm := rf.currentTerm
	args.Term = rf.currentTerm
	args.LeaderId = rf.me
	ni := rf.nextIndex[server]
	args.PrevLogIndex = ni
	args.LeaderCommit = rf.commitIndex

	if args.PrevLogIndex == -1 {
		args.PrevLogTerm = -1
	} else {
		args.PrevLogTerm = rf.entries[ni].Term
	}
	entries := rf.entries[ni+1:]
	args.Entries = entries
	rf.mu.Unlock()
	if rf.state!=1 {
		return false
	}
	ok := rf.peers[server].Call("Raft.HeartbeatHandler", args, reply)
	if !ok {
		return ok
	}
	rf.mu.Lock()
	defer rf.mu.Unlock()
	if reply.Term > rf.currentTerm {
		rf.turnToFollower(reply.Term)
		return ok
	} 
	if rf.state!=1 || savedCurrentTerm!=reply.Term {
		return ok
	}
	if !reply.Success  {
		rf.nextIndex[server] = ni - 1
	} else {
		rf.nextIndex[server]=ni+len(entries)
		rf.matchIndex[server]=rf.nextIndex[server]

		savedCommitIndex := rf.commitIndex
		for i:=rf.commitIndex+1; i<len(rf.entries); i++{
			if rf.entries[i].Term==rf.currentTerm {
				
				matchCount :=1
				for j,_  := range rf.peers {
					if rf.matchIndex[j]>=i {
						matchCount++
					}
				}
				if matchCount > rf.serverNum/2 {
					rf.commitIndex=i
				}
			}
		}
		for i:=savedCommitIndex+1; i<=rf.commitIndex; i++ {
			rf.ch <- ApplyMsg{CommandValid: true, Command:rf.entries[i].Command, CommandIndex:i}
		}
	}
	return ok
}

func (rf *Raft) HeartbeatHandler(args *HeartbeatArgs, reply *HeartbeatReply) {
	rf.mu.Lock()
	defer rf.mu.Unlock()
	if rf.killed() {
		return
	}
	if args.Term > rf.currentTerm {

		rf.turnToFollower(args.Term)
	}
	reply.Success = false
	if args.Term == rf.currentTerm {
		if rf.state!=0 {
			rf.turnToFollower(args.Term)
		}
		rf.startTime = time.Now()
		if args.PrevLogIndex < len(rf.entries) && rf.entries[args.PrevLogIndex].Term == args.PrevLogTerm {
			reply.Success = true
			logInsertIndex := args.PrevLogIndex + 1
			newEntriesIndex := 0

			for {
				if logInsertIndex >= len(rf.entries) || newEntriesIndex >= len(args.Entries) {
					break
				}
				if rf.entries[logInsertIndex].Term != args.Entries[newEntriesIndex].Term {
					break
				}
				logInsertIndex++
				newEntriesIndex++
			}
			if newEntriesIndex < len(args.Entries) {
				rf.entries = append(rf.entries[:logInsertIndex], args.Entries[newEntriesIndex:]...)
				
			}
			if args.LeaderCommit > rf.commitIndex {
				for i:=rf.commitIndex+1; i<=args.LeaderCommit;i++ {

					rf.ch <- ApplyMsg{CommandValid: true, Command:rf.entries[i].Command, CommandIndex:i}
				}
				rf.commitIndex = args.LeaderCommit
			}
			
		}
	}
	reply.Term = rf.currentTerm
	rf.persist()
}

func (rf *Raft) turnToFollower (term int) {

	rf.startTime = time.Now()
	rf.state = 0
	rf.currentTerm = term
	
	rf.voteFor = -1
	rf.voteNum = 0
	go rf.runElectionTimer()

}
