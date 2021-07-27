package kvraft

type GetArgs struct {
	Key string

	ClientId int64
	ReqId int
}

type GetReply struct {
	Value string
	IsLeader bool
}

type PutAppendArgs struct {
	ClientId int64
	ReqId int
	Op string
	Key string
	Value string
}

type PutAppendReply struct {
	IsLeader bool
}


