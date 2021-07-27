package kvraft

import (
	"../labgob"
	"../labrpc"
	"log"
	"../raft"
	"sync"
	"sync/atomic"
	"time"
)

const Debug = 0

func DPrintf(format string, a ...interface{}) (n int, err error) {
	if Debug > 0 {
		log.Printf(format, a...)
	}
	return
}

type Op struct {
	Operation string
	Key string
	Value string
	ClientId int64
	ReqId int
}

type KVServer struct {
	mu      sync.Mutex
	me      int
	rf      *raft.Raft
	applyCh chan raft.ApplyMsg
	dead    int32 

	maxraftstate int 

	// Your definitions here.
	data map[string]string			//repo storing the Key/Value
	cache map[int64]int			//a cache for processed requests
	commitChan map[int]chan Op	//there will be one channel for each command, the key is command index
}

func (kv *KVServer) AppendEntryToLog(entry Op) bool {
	index, _, isLeader := kv.rf.Start(entry)
	if !isLeader {
		return false
	}
	kv.mu.Lock()
	ch, ok := kv.commitChan[index]
	if !ok {
		ch = make(chan Op, 1)
		kv.commitChan[index] = ch
	}
	kv.mu.Unlock()

	select {
	case op := <-ch:
		return op == entry
	case <-time.After(1000 *time.Millisecond):
		return false
	}
}

func (kv *KVServer) Get(args *GetArgs, reply *GetReply) {
	// Your code here.
	entry := Op{Operation: "Get", Key: args.Key, ClientId: args.ClientId, ReqId: args.ReqId}
	ok := kv.AppendEntryToLog(entry)
	if ok {
		reply.IsLeader = true
		kv.mu.Lock()
		val, ok := kv.data[args.Key]
		if ok {
			reply.Value = val
		} else {
			reply.Value = ""
		}
		kv.mu.Unlock()
	} else {
		reply.IsLeader = false
	}
}

func (kv *KVServer) PutAppend(args *PutAppendArgs, reply *PutAppendReply) {
	// Your code here.
	entry := Op{Operation: args.Op, Key: args.Key, Value: args.Value, ClientId: args.ClientId, ReqId: args.ReqId}
	ok := kv.AppendEntryToLog(entry)
	if !ok {
		reply.IsLeader = false
	} else {
		reply.IsLeader = true
	}
}


func (kv *KVServer) Kill() {
	atomic.StoreInt32(&kv.dead, 1)
	kv.rf.Kill()
}

func (kv *KVServer) killed() bool {
	z := atomic.LoadInt32(&kv.dead)
	return z == 1
}

func (kv *KVServer) listen() {
	for {
		if kv.killed() {
			break
		}
		msg := <-kv.applyCh
		cmd := msg.Command.(Op)
		kv.mu.Lock()
		v, ok := kv.cache[cmd.ClientId]

		if !ok || v < cmd.ReqId {
			if cmd.Operation == "Put" {
				kv.data[cmd.Key] = cmd.Value
			} else if cmd.Operation == "Append" {
				kv.data[cmd.Key] += cmd.Value
			}

			kv.cache[cmd.ClientId] = cmd.ReqId
		}
		ch, ok := kv.commitChan[msg.CommandIndex]
		if ok {
			ch <- cmd
		} else {
			kv.commitChan[msg.CommandIndex] = make(chan Op, 1)
		}
		kv.mu.Unlock()
	}
}

func StartKVServer(servers []*labrpc.ClientEnd, me int, persister *raft.Persister, maxraftstate int) *KVServer {

	labgob.Register(Op{})

	kv := new(KVServer)
	kv.me = me
	kv.maxraftstate = maxraftstate

	// You may need initialization code here.
	kv.applyCh = make(chan raft.ApplyMsg)
	kv.rf = raft.Make(servers, me, persister, kv.applyCh)

	kv.data = make(map[string]string)
	kv.cache = make(map[int64]int)
	kv.commitChan = make(map[int]chan Op)

	go kv.listen()
	return kv
}