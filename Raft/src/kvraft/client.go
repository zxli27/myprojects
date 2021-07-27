package kvraft

import "../labrpc"
import "crypto/rand"
import "math/big"
import "sync"


type Clerk struct {
	mu sync.Mutex
	servers []*labrpc.ClientEnd

	clientId int64
	reqId int
	leaderId int
}

func nrand() int64 {
	max := big.NewInt(int64(1) << 62)
	bigx, _ := rand.Int(rand.Reader, max)
	x := bigx.Int64()
	return x
}

func MakeClerk(servers []*labrpc.ClientEnd) *Clerk {
	ck := new(Clerk)
	ck.servers = servers

	ck.clientId = nrand()
	ck.leaderId = -1
	ck.reqId = 0
	return ck
}


func (ck *Clerk) Get(key string) string {
	args := GetArgs{Key: key, ClientId: ck.clientId}

	ck.mu.Lock()
	args.ReqId = ck.reqId
	ck.reqId++
	ck.mu.Unlock()
	for {
		if ck.leaderId >=0 {
			var reply GetReply
			ok := ck.servers[ck.leaderId].Call("KVServer.Get", &args, &reply) 
			if ok && reply.IsLeader {
				return reply.Value
			} else {
				ck.leaderId = -1
			}
		}
		for i, v := range ck.servers {
			var reply GetReply
			ok := v.Call("KVServer.Get", &args, &reply)
			if ok && reply.IsLeader {
				ck.leaderId = i
				return reply.Value
			}
		}
	}
}

func (ck *Clerk) PutAppend(key string, value string, op string) {

	args := PutAppendArgs{Key: key, ClientId: ck.clientId, Value: value, Op: op}

	ck.mu.Lock()
	args.ReqId = ck.reqId
	ck.reqId++
	ck.mu.Unlock()
	for {
		if ck.leaderId >=0 {
			var reply GetReply
			ok := ck.servers[ck.leaderId].Call("KVServer.PutAppend", &args, &reply) 
			if ok && reply.IsLeader {
				return 
			} else {
				ck.leaderId = -1
			}
		}
		for i, v := range ck.servers {
			var reply PutAppendReply
			ok := v.Call("KVServer.PutAppend", &args, &reply)
			if ok && reply.IsLeader {
				ck.leaderId = i
				return
			}
		}
	}
}

func (ck *Clerk) Put(key string, value string) {
	ck.PutAppend(key, value, "Put")
}
func (ck *Clerk) Append(key string, value string) {
	ck.PutAppend(key, value, "Append")
}