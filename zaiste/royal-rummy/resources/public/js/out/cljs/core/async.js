// Compiled by ClojureScript 0.0-2511
goog.provide('cljs.core.async');
goog.require('cljs.core');
goog.require('cljs.core.async.impl.channels');
goog.require('cljs.core.async.impl.dispatch');
goog.require('cljs.core.async.impl.ioc_helpers');
goog.require('cljs.core.async.impl.protocols');
goog.require('cljs.core.async.impl.buffers');
goog.require('cljs.core.async.impl.timers');
cljs.core.async.fn_handler = (function fn_handler(f){
if(typeof cljs.core.async.t12266 !== 'undefined'){
} else {

/**
* @constructor
*/
cljs.core.async.t12266 = (function (f,fn_handler,meta12267){
this.f = f;
this.fn_handler = fn_handler;
this.meta12267 = meta12267;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
cljs.core.async.t12266.prototype.cljs$core$async$impl$protocols$Handler$ = true;

cljs.core.async.t12266.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return true;
});

cljs.core.async.t12266.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.f;
});

cljs.core.async.t12266.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_12268){
var self__ = this;
var _12268__$1 = this;
return self__.meta12267;
});

cljs.core.async.t12266.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_12268,meta12267__$1){
var self__ = this;
var _12268__$1 = this;
return (new cljs.core.async.t12266(self__.f,self__.fn_handler,meta12267__$1));
});

cljs.core.async.t12266.cljs$lang$type = true;

cljs.core.async.t12266.cljs$lang$ctorStr = "cljs.core.async/t12266";

cljs.core.async.t12266.cljs$lang$ctorPrWriter = (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"cljs.core.async/t12266");
});

cljs.core.async.__GT_t12266 = (function __GT_t12266(f__$1,fn_handler__$1,meta12267){
return (new cljs.core.async.t12266(f__$1,fn_handler__$1,meta12267));
});

}

return (new cljs.core.async.t12266(f,fn_handler,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"line","line",212345235),13,new cljs.core.Keyword(null,"column","column",2078222095),3,new cljs.core.Keyword(null,"end-line","end-line",1837326455),16,new cljs.core.Keyword(null,"end-column","end-column",1425389514),20], null)));
});
/**
* Returns a fixed buffer of size n. When full, puts will block/park.
*/
cljs.core.async.buffer = (function buffer(n){
return cljs.core.async.impl.buffers.fixed_buffer.call(null,n);
});
/**
* Returns a buffer of size n. When full, puts will complete but
* val will be dropped (no transfer).
*/
cljs.core.async.dropping_buffer = (function dropping_buffer(n){
return cljs.core.async.impl.buffers.dropping_buffer.call(null,n);
});
/**
* Returns a buffer of size n. When full, puts will complete, and be
* buffered, but oldest elements in buffer will be dropped (not
* transferred).
*/
cljs.core.async.sliding_buffer = (function sliding_buffer(n){
return cljs.core.async.impl.buffers.sliding_buffer.call(null,n);
});
/**
* Returns true if a channel created with buff will never block. That is to say,
* puts into this buffer will never cause the buffer to be full.
*/
cljs.core.async.unblocking_buffer_QMARK_ = (function unblocking_buffer_QMARK_(buff){
var G__12270 = buff;
if(G__12270){
var bit__4311__auto__ = null;
if(cljs.core.truth_((function (){var or__3630__auto__ = bit__4311__auto__;
if(cljs.core.truth_(or__3630__auto__)){
return or__3630__auto__;
} else {
return G__12270.cljs$core$async$impl$protocols$UnblockingBuffer$;
}
})())){
return true;
} else {
if((!G__12270.cljs$lang$protocol_mask$partition$)){
return cljs.core.native_satisfies_QMARK_.call(null,cljs.core.async.impl.protocols.UnblockingBuffer,G__12270);
} else {
return false;
}
}
} else {
return cljs.core.native_satisfies_QMARK_.call(null,cljs.core.async.impl.protocols.UnblockingBuffer,G__12270);
}
});
/**
* Creates a channel with an optional buffer, an optional transducer (like (map f),
* (filter p) etc or a composition thereof), and an optional exception handler.
* If buf-or-n is a number, will create and use a fixed buffer of that size. If a
* transducer is supplied a buffer must be specified. ex-handler must be a
* fn of one argument - if an exception occurs during transformation it will be called
* with the thrown value as an argument, and any non-nil return value will be placed
* in the channel.
*/
cljs.core.async.chan = (function() {
var chan = null;
var chan__0 = (function (){
return chan.call(null,null);
});
var chan__1 = (function (buf_or_n){
return chan.call(null,buf_or_n,null,null);
});
var chan__2 = (function (buf_or_n,xform){
return chan.call(null,buf_or_n,xform,null);
});
var chan__3 = (function (buf_or_n,xform,ex_handler){
var buf_or_n__$1 = ((cljs.core._EQ_.call(null,buf_or_n,(0)))?null:buf_or_n);
if(cljs.core.truth_(xform)){
if(cljs.core.truth_(buf_or_n__$1)){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("buffer must be supplied when transducer is"),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.call(null,new cljs.core.Symbol(null,"buf-or-n","buf-or-n",-1646815050,null)))].join('')));
}
} else {
}

return cljs.core.async.impl.channels.chan.call(null,((typeof buf_or_n__$1 === 'number')?cljs.core.async.buffer.call(null,buf_or_n__$1):buf_or_n__$1),xform,ex_handler);
});
chan = function(buf_or_n,xform,ex_handler){
switch(arguments.length){
case 0:
return chan__0.call(this);
case 1:
return chan__1.call(this,buf_or_n);
case 2:
return chan__2.call(this,buf_or_n,xform);
case 3:
return chan__3.call(this,buf_or_n,xform,ex_handler);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
chan.cljs$core$IFn$_invoke$arity$0 = chan__0;
chan.cljs$core$IFn$_invoke$arity$1 = chan__1;
chan.cljs$core$IFn$_invoke$arity$2 = chan__2;
chan.cljs$core$IFn$_invoke$arity$3 = chan__3;
return chan;
})()
;
/**
* Returns a channel that will close after msecs
*/
cljs.core.async.timeout = (function timeout(msecs){
return cljs.core.async.impl.timers.timeout.call(null,msecs);
});
/**
* takes a val from port. Must be called inside a (go ...) block. Will
* return nil if closed. Will park if nothing is available.
* Returns true unless port is already closed
*/
cljs.core.async._LT__BANG_ = (function _LT__BANG_(port){
throw (new Error("<! used not in (go ...) block"));
});
/**
* Asynchronously takes a val from port, passing to fn1. Will pass nil
* if closed. If on-caller? (default true) is true, and value is
* immediately available, will call fn1 on calling thread.
* Returns nil.
*/
cljs.core.async.take_BANG_ = (function() {
var take_BANG_ = null;
var take_BANG___2 = (function (port,fn1){
return take_BANG_.call(null,port,fn1,true);
});
var take_BANG___3 = (function (port,fn1,on_caller_QMARK_){
var ret = cljs.core.async.impl.protocols.take_BANG_.call(null,port,cljs.core.async.fn_handler.call(null,fn1));
if(cljs.core.truth_(ret)){
var val_12271 = cljs.core.deref.call(null,ret);
if(cljs.core.truth_(on_caller_QMARK_)){
fn1.call(null,val_12271);
} else {
cljs.core.async.impl.dispatch.run.call(null,((function (val_12271,ret){
return (function (){
return fn1.call(null,val_12271);
});})(val_12271,ret))
);
}
} else {
}

return null;
});
take_BANG_ = function(port,fn1,on_caller_QMARK_){
switch(arguments.length){
case 2:
return take_BANG___2.call(this,port,fn1);
case 3:
return take_BANG___3.call(this,port,fn1,on_caller_QMARK_);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
take_BANG_.cljs$core$IFn$_invoke$arity$2 = take_BANG___2;
take_BANG_.cljs$core$IFn$_invoke$arity$3 = take_BANG___3;
return take_BANG_;
})()
;
cljs.core.async.nop = (function nop(_){
return null;
});
cljs.core.async.fhnop = cljs.core.async.fn_handler.call(null,cljs.core.async.nop);
/**
* puts a val into port. nil values are not allowed. Must be called
* inside a (go ...) block. Will park if no buffer space is available.
* Returns true unless port is already closed.
*/
cljs.core.async._GT__BANG_ = (function _GT__BANG_(port,val){
throw (new Error(">! used not in (go ...) block"));
});
/**
* Asynchronously puts a val into port, calling fn0 (if supplied) when
* complete. nil values are not allowed. Will throw if closed. If
* on-caller? (default true) is true, and the put is immediately
* accepted, will call fn0 on calling thread.  Returns nil.
*/
cljs.core.async.put_BANG_ = (function() {
var put_BANG_ = null;
var put_BANG___2 = (function (port,val){
var temp__4124__auto__ = cljs.core.async.impl.protocols.put_BANG_.call(null,port,val,cljs.core.async.fhnop);
if(cljs.core.truth_(temp__4124__auto__)){
var ret = temp__4124__auto__;
return cljs.core.deref.call(null,ret);
} else {
return true;
}
});
var put_BANG___3 = (function (port,val,fn1){
return put_BANG_.call(null,port,val,fn1,true);
});
var put_BANG___4 = (function (port,val,fn1,on_caller_QMARK_){
var temp__4124__auto__ = cljs.core.async.impl.protocols.put_BANG_.call(null,port,val,cljs.core.async.fn_handler.call(null,fn1));
if(cljs.core.truth_(temp__4124__auto__)){
var retb = temp__4124__auto__;
var ret = cljs.core.deref.call(null,retb);
if(cljs.core.truth_(on_caller_QMARK_)){
fn1.call(null,ret);
} else {
cljs.core.async.impl.dispatch.run.call(null,((function (ret,retb,temp__4124__auto__){
return (function (){
return fn1.call(null,ret);
});})(ret,retb,temp__4124__auto__))
);
}

return ret;
} else {
return true;
}
});
put_BANG_ = function(port,val,fn1,on_caller_QMARK_){
switch(arguments.length){
case 2:
return put_BANG___2.call(this,port,val);
case 3:
return put_BANG___3.call(this,port,val,fn1);
case 4:
return put_BANG___4.call(this,port,val,fn1,on_caller_QMARK_);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
put_BANG_.cljs$core$IFn$_invoke$arity$2 = put_BANG___2;
put_BANG_.cljs$core$IFn$_invoke$arity$3 = put_BANG___3;
put_BANG_.cljs$core$IFn$_invoke$arity$4 = put_BANG___4;
return put_BANG_;
})()
;
cljs.core.async.close_BANG_ = (function close_BANG_(port){
return cljs.core.async.impl.protocols.close_BANG_.call(null,port);
});
cljs.core.async.random_array = (function random_array(n){
var a = (new Array(n));
var n__4517__auto___12272 = n;
var x_12273 = (0);
while(true){
if((x_12273 < n__4517__auto___12272)){
(a[x_12273] = (0));

var G__12274 = (x_12273 + (1));
x_12273 = G__12274;
continue;
} else {
}
break;
}

var i = (1);
while(true){
if(cljs.core._EQ_.call(null,i,n)){
return a;
} else {
var j = cljs.core.rand_int.call(null,i);
(a[i] = (a[j]));

(a[j] = i);

var G__12275 = (i + (1));
i = G__12275;
continue;
}
break;
}
});
cljs.core.async.alt_flag = (function alt_flag(){
var flag = cljs.core.atom.call(null,true);
if(typeof cljs.core.async.t12279 !== 'undefined'){
} else {

/**
* @constructor
*/
cljs.core.async.t12279 = (function (flag,alt_flag,meta12280){
this.flag = flag;
this.alt_flag = alt_flag;
this.meta12280 = meta12280;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
cljs.core.async.t12279.prototype.cljs$core$async$impl$protocols$Handler$ = true;

cljs.core.async.t12279.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = ((function (flag){
return (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.deref.call(null,self__.flag);
});})(flag))
;

cljs.core.async.t12279.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = ((function (flag){
return (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_.call(null,self__.flag,null);

return true;
});})(flag))
;

cljs.core.async.t12279.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (flag){
return (function (_12281){
var self__ = this;
var _12281__$1 = this;
return self__.meta12280;
});})(flag))
;

cljs.core.async.t12279.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (flag){
return (function (_12281,meta12280__$1){
var self__ = this;
var _12281__$1 = this;
return (new cljs.core.async.t12279(self__.flag,self__.alt_flag,meta12280__$1));
});})(flag))
;

cljs.core.async.t12279.cljs$lang$type = true;

cljs.core.async.t12279.cljs$lang$ctorStr = "cljs.core.async/t12279";

cljs.core.async.t12279.cljs$lang$ctorPrWriter = ((function (flag){
return (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"cljs.core.async/t12279");
});})(flag))
;

cljs.core.async.__GT_t12279 = ((function (flag){
return (function __GT_t12279(flag__$1,alt_flag__$1,meta12280){
return (new cljs.core.async.t12279(flag__$1,alt_flag__$1,meta12280));
});})(flag))
;

}

return (new cljs.core.async.t12279(flag,alt_flag,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"line","line",212345235),141,new cljs.core.Keyword(null,"column","column",2078222095),5,new cljs.core.Keyword(null,"end-line","end-line",1837326455),146,new cljs.core.Keyword(null,"end-column","end-column",1425389514),15], null)));
});
cljs.core.async.alt_handler = (function alt_handler(flag,cb){
if(typeof cljs.core.async.t12285 !== 'undefined'){
} else {

/**
* @constructor
*/
cljs.core.async.t12285 = (function (cb,flag,alt_handler,meta12286){
this.cb = cb;
this.flag = flag;
this.alt_handler = alt_handler;
this.meta12286 = meta12286;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
cljs.core.async.t12285.prototype.cljs$core$async$impl$protocols$Handler$ = true;

cljs.core.async.t12285.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.active_QMARK_.call(null,self__.flag);
});

cljs.core.async.t12285.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.async.impl.protocols.commit.call(null,self__.flag);

return self__.cb;
});

cljs.core.async.t12285.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_12287){
var self__ = this;
var _12287__$1 = this;
return self__.meta12286;
});

cljs.core.async.t12285.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_12287,meta12286__$1){
var self__ = this;
var _12287__$1 = this;
return (new cljs.core.async.t12285(self__.cb,self__.flag,self__.alt_handler,meta12286__$1));
});

cljs.core.async.t12285.cljs$lang$type = true;

cljs.core.async.t12285.cljs$lang$ctorStr = "cljs.core.async/t12285";

cljs.core.async.t12285.cljs$lang$ctorPrWriter = (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"cljs.core.async/t12285");
});

cljs.core.async.__GT_t12285 = (function __GT_t12285(cb__$1,flag__$1,alt_handler__$1,meta12286){
return (new cljs.core.async.t12285(cb__$1,flag__$1,alt_handler__$1,meta12286));
});

}

return (new cljs.core.async.t12285(cb,flag,alt_handler,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"line","line",212345235),149,new cljs.core.Keyword(null,"column","column",2078222095),3,new cljs.core.Keyword(null,"end-line","end-line",1837326455),154,new cljs.core.Keyword(null,"end-column","end-column",1425389514),11], null)));
});
/**
* returns derefable [val port] if immediate, nil if enqueued
*/
cljs.core.async.do_alts = (function do_alts(fret,ports,opts){
var flag = cljs.core.async.alt_flag.call(null);
var n = cljs.core.count.call(null,ports);
var idxs = cljs.core.async.random_array.call(null,n);
var priority = new cljs.core.Keyword(null,"priority","priority",1431093715).cljs$core$IFn$_invoke$arity$1(opts);
var ret = (function (){var i = (0);
while(true){
if((i < n)){
var idx = (cljs.core.truth_(priority)?i:(idxs[i]));
var port = cljs.core.nth.call(null,ports,idx);
var wport = ((cljs.core.vector_QMARK_.call(null,port))?port.call(null,(0)):null);
var vbox = (cljs.core.truth_(wport)?(function (){var val = port.call(null,(1));
return cljs.core.async.impl.protocols.put_BANG_.call(null,wport,val,cljs.core.async.alt_handler.call(null,flag,((function (i,val,idx,port,wport,flag,n,idxs,priority){
return (function (p1__12288_SHARP_){
return fret.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [p1__12288_SHARP_,wport], null));
});})(i,val,idx,port,wport,flag,n,idxs,priority))
));
})():cljs.core.async.impl.protocols.take_BANG_.call(null,port,cljs.core.async.alt_handler.call(null,flag,((function (i,idx,port,wport,flag,n,idxs,priority){
return (function (p1__12289_SHARP_){
return fret.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [p1__12289_SHARP_,port], null));
});})(i,idx,port,wport,flag,n,idxs,priority))
)));
if(cljs.core.truth_(vbox)){
return cljs.core.async.impl.channels.box.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.deref.call(null,vbox),(function (){var or__3630__auto__ = wport;
if(cljs.core.truth_(or__3630__auto__)){
return or__3630__auto__;
} else {
return port;
}
})()], null));
} else {
var G__12290 = (i + (1));
i = G__12290;
continue;
}
} else {
return null;
}
break;
}
})();
var or__3630__auto__ = ret;
if(cljs.core.truth_(or__3630__auto__)){
return or__3630__auto__;
} else {
if(cljs.core.contains_QMARK_.call(null,opts,new cljs.core.Keyword(null,"default","default",-1987822328))){
var temp__4126__auto__ = (function (){var and__3618__auto__ = cljs.core.async.impl.protocols.active_QMARK_.call(null,flag);
if(cljs.core.truth_(and__3618__auto__)){
return cljs.core.async.impl.protocols.commit.call(null,flag);
} else {
return and__3618__auto__;
}
})();
if(cljs.core.truth_(temp__4126__auto__)){
var got = temp__4126__auto__;
return cljs.core.async.impl.channels.box.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"default","default",-1987822328).cljs$core$IFn$_invoke$arity$1(opts),new cljs.core.Keyword(null,"default","default",-1987822328)], null));
} else {
return null;
}
} else {
return null;
}
}
});
/**
* Completes at most one of several channel operations. Must be called
* inside a (go ...) block. ports is a vector of channel endpoints,
* which can be either a channel to take from or a vector of
* [channel-to-put-to val-to-put], in any combination. Takes will be
* made as if by <!, and puts will be made as if by >!. Unless
* the :priority option is true, if more than one port operation is
* ready a non-deterministic choice will be made. If no operation is
* ready and a :default value is supplied, [default-val :default] will
* be returned, otherwise alts! will park until the first operation to
* become ready completes. Returns [val port] of the completed
* operation, where val is the value taken for takes, and a
* boolean (true unless already closed, as per put!) for puts.
* 
* opts are passed as :key val ... Supported options:
* 
* :default val - the value to use if none of the operations are immediately ready
* :priority true - (default nil) when true, the operations will be tried in order.
* 
* Note: there is no guarantee that the port exps or val exprs will be
* used, nor in what order should they be, so they should not be
* depended upon for side effects.
* @param {...*} var_args
*/
cljs.core.async.alts_BANG_ = (function() { 
var alts_BANG___delegate = function (ports,p__12291){
var map__12293 = p__12291;
var map__12293__$1 = ((cljs.core.seq_QMARK_.call(null,map__12293))?cljs.core.apply.call(null,cljs.core.hash_map,map__12293):map__12293);
var opts = map__12293__$1;
throw (new Error("alts! used not in (go ...) block"));
};
var alts_BANG_ = function (ports,var_args){
var p__12291 = null;
if (arguments.length > 1) {
  p__12291 = cljs.core.array_seq(Array.prototype.slice.call(arguments, 1),0);
} 
return alts_BANG___delegate.call(this,ports,p__12291);};
alts_BANG_.cljs$lang$maxFixedArity = 1;
alts_BANG_.cljs$lang$applyTo = (function (arglist__12294){
var ports = cljs.core.first(arglist__12294);
var p__12291 = cljs.core.rest(arglist__12294);
return alts_BANG___delegate(ports,p__12291);
});
alts_BANG_.cljs$core$IFn$_invoke$arity$variadic = alts_BANG___delegate;
return alts_BANG_;
})()
;
/**
* Takes elements from the from channel and supplies them to the to
* channel. By default, the to channel will be closed when the from
* channel closes, but can be determined by the close?  parameter. Will
* stop consuming the from channel if the to channel closes
*/
cljs.core.async.pipe = (function() {
var pipe = null;
var pipe__2 = (function (from,to){
return pipe.call(null,from,to,true);
});
var pipe__3 = (function (from,to,close_QMARK_){
var c__6505__auto___12389 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___12389){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___12389){
return (function (state_12365){
var state_val_12366 = (state_12365[(1)]);
if((state_val_12366 === (7))){
var inst_12361 = (state_12365[(2)]);
var state_12365__$1 = state_12365;
var statearr_12367_12390 = state_12365__$1;
(statearr_12367_12390[(2)] = inst_12361);

(statearr_12367_12390[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12366 === (1))){
var state_12365__$1 = state_12365;
var statearr_12368_12391 = state_12365__$1;
(statearr_12368_12391[(2)] = null);

(statearr_12368_12391[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12366 === (4))){
var inst_12344 = (state_12365[(7)]);
var inst_12344__$1 = (state_12365[(2)]);
var inst_12345 = (inst_12344__$1 == null);
var state_12365__$1 = (function (){var statearr_12369 = state_12365;
(statearr_12369[(7)] = inst_12344__$1);

return statearr_12369;
})();
if(cljs.core.truth_(inst_12345)){
var statearr_12370_12392 = state_12365__$1;
(statearr_12370_12392[(1)] = (5));

} else {
var statearr_12371_12393 = state_12365__$1;
(statearr_12371_12393[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12366 === (13))){
var state_12365__$1 = state_12365;
var statearr_12372_12394 = state_12365__$1;
(statearr_12372_12394[(2)] = null);

(statearr_12372_12394[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12366 === (6))){
var inst_12344 = (state_12365[(7)]);
var state_12365__$1 = state_12365;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_12365__$1,(11),to,inst_12344);
} else {
if((state_val_12366 === (3))){
var inst_12363 = (state_12365[(2)]);
var state_12365__$1 = state_12365;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_12365__$1,inst_12363);
} else {
if((state_val_12366 === (12))){
var state_12365__$1 = state_12365;
var statearr_12373_12395 = state_12365__$1;
(statearr_12373_12395[(2)] = null);

(statearr_12373_12395[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12366 === (2))){
var state_12365__$1 = state_12365;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_12365__$1,(4),from);
} else {
if((state_val_12366 === (11))){
var inst_12354 = (state_12365[(2)]);
var state_12365__$1 = state_12365;
if(cljs.core.truth_(inst_12354)){
var statearr_12374_12396 = state_12365__$1;
(statearr_12374_12396[(1)] = (12));

} else {
var statearr_12375_12397 = state_12365__$1;
(statearr_12375_12397[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12366 === (9))){
var state_12365__$1 = state_12365;
var statearr_12376_12398 = state_12365__$1;
(statearr_12376_12398[(2)] = null);

(statearr_12376_12398[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12366 === (5))){
var state_12365__$1 = state_12365;
if(cljs.core.truth_(close_QMARK_)){
var statearr_12377_12399 = state_12365__$1;
(statearr_12377_12399[(1)] = (8));

} else {
var statearr_12378_12400 = state_12365__$1;
(statearr_12378_12400[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12366 === (14))){
var inst_12359 = (state_12365[(2)]);
var state_12365__$1 = state_12365;
var statearr_12379_12401 = state_12365__$1;
(statearr_12379_12401[(2)] = inst_12359);

(statearr_12379_12401[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12366 === (10))){
var inst_12351 = (state_12365[(2)]);
var state_12365__$1 = state_12365;
var statearr_12380_12402 = state_12365__$1;
(statearr_12380_12402[(2)] = inst_12351);

(statearr_12380_12402[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12366 === (8))){
var inst_12348 = cljs.core.async.close_BANG_.call(null,to);
var state_12365__$1 = state_12365;
var statearr_12381_12403 = state_12365__$1;
(statearr_12381_12403[(2)] = inst_12348);

(statearr_12381_12403[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___12389))
;
return ((function (switch__6490__auto__,c__6505__auto___12389){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_12385 = [null,null,null,null,null,null,null,null];
(statearr_12385[(0)] = state_machine__6491__auto__);

(statearr_12385[(1)] = (1));

return statearr_12385;
});
var state_machine__6491__auto____1 = (function (state_12365){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_12365);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e12386){if((e12386 instanceof Object)){
var ex__6494__auto__ = e12386;
var statearr_12387_12404 = state_12365;
(statearr_12387_12404[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_12365);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e12386;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__12405 = state_12365;
state_12365 = G__12405;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_12365){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_12365);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___12389))
})();
var state__6507__auto__ = (function (){var statearr_12388 = f__6506__auto__.call(null);
(statearr_12388[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___12389);

return statearr_12388;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___12389))
);


return to;
});
pipe = function(from,to,close_QMARK_){
switch(arguments.length){
case 2:
return pipe__2.call(this,from,to);
case 3:
return pipe__3.call(this,from,to,close_QMARK_);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
pipe.cljs$core$IFn$_invoke$arity$2 = pipe__2;
pipe.cljs$core$IFn$_invoke$arity$3 = pipe__3;
return pipe;
})()
;
cljs.core.async.pipeline_STAR_ = (function pipeline_STAR_(n,to,xf,from,close_QMARK_,ex_handler,type){
if((n > (0))){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str(cljs.core.pr_str.call(null,cljs.core.list(new cljs.core.Symbol(null,"pos?","pos?",-244377722,null),new cljs.core.Symbol(null,"n","n",-2092305744,null))))].join('')));
}

var jobs = cljs.core.async.chan.call(null,n);
var results = cljs.core.async.chan.call(null,n);
var process = ((function (jobs,results){
return (function (p__12589){
var vec__12590 = p__12589;
var v = cljs.core.nth.call(null,vec__12590,(0),null);
var p = cljs.core.nth.call(null,vec__12590,(1),null);
var job = vec__12590;
if((job == null)){
cljs.core.async.close_BANG_.call(null,results);

return null;
} else {
var res = cljs.core.async.chan.call(null,(1),xf,ex_handler);
var c__6505__auto___12772 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___12772,res,vec__12590,v,p,job,jobs,results){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___12772,res,vec__12590,v,p,job,jobs,results){
return (function (state_12595){
var state_val_12596 = (state_12595[(1)]);
if((state_val_12596 === (2))){
var inst_12592 = (state_12595[(2)]);
var inst_12593 = cljs.core.async.close_BANG_.call(null,res);
var state_12595__$1 = (function (){var statearr_12597 = state_12595;
(statearr_12597[(7)] = inst_12592);

return statearr_12597;
})();
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_12595__$1,inst_12593);
} else {
if((state_val_12596 === (1))){
var state_12595__$1 = state_12595;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_12595__$1,(2),res,v);
} else {
return null;
}
}
});})(c__6505__auto___12772,res,vec__12590,v,p,job,jobs,results))
;
return ((function (switch__6490__auto__,c__6505__auto___12772,res,vec__12590,v,p,job,jobs,results){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_12601 = [null,null,null,null,null,null,null,null];
(statearr_12601[(0)] = state_machine__6491__auto__);

(statearr_12601[(1)] = (1));

return statearr_12601;
});
var state_machine__6491__auto____1 = (function (state_12595){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_12595);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e12602){if((e12602 instanceof Object)){
var ex__6494__auto__ = e12602;
var statearr_12603_12773 = state_12595;
(statearr_12603_12773[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_12595);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e12602;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__12774 = state_12595;
state_12595 = G__12774;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_12595){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_12595);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___12772,res,vec__12590,v,p,job,jobs,results))
})();
var state__6507__auto__ = (function (){var statearr_12604 = f__6506__auto__.call(null);
(statearr_12604[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___12772);

return statearr_12604;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___12772,res,vec__12590,v,p,job,jobs,results))
);


cljs.core.async.put_BANG_.call(null,p,res);

return true;
}
});})(jobs,results))
;
var async = ((function (jobs,results,process){
return (function (p__12605){
var vec__12606 = p__12605;
var v = cljs.core.nth.call(null,vec__12606,(0),null);
var p = cljs.core.nth.call(null,vec__12606,(1),null);
var job = vec__12606;
if((job == null)){
cljs.core.async.close_BANG_.call(null,results);

return null;
} else {
var res = cljs.core.async.chan.call(null,(1));
xf.call(null,v,res);

cljs.core.async.put_BANG_.call(null,p,res);

return true;
}
});})(jobs,results,process))
;
var n__4517__auto___12775 = n;
var __12776 = (0);
while(true){
if((__12776 < n__4517__auto___12775)){
var G__12607_12777 = (((type instanceof cljs.core.Keyword))?type.fqn:null);
switch (G__12607_12777) {
case "async":
var c__6505__auto___12779 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (__12776,c__6505__auto___12779,G__12607_12777,n__4517__auto___12775,jobs,results,process,async){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (__12776,c__6505__auto___12779,G__12607_12777,n__4517__auto___12775,jobs,results,process,async){
return (function (state_12620){
var state_val_12621 = (state_12620[(1)]);
if((state_val_12621 === (7))){
var inst_12616 = (state_12620[(2)]);
var state_12620__$1 = state_12620;
var statearr_12622_12780 = state_12620__$1;
(statearr_12622_12780[(2)] = inst_12616);

(statearr_12622_12780[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12621 === (6))){
var state_12620__$1 = state_12620;
var statearr_12623_12781 = state_12620__$1;
(statearr_12623_12781[(2)] = null);

(statearr_12623_12781[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12621 === (5))){
var state_12620__$1 = state_12620;
var statearr_12624_12782 = state_12620__$1;
(statearr_12624_12782[(2)] = null);

(statearr_12624_12782[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12621 === (4))){
var inst_12610 = (state_12620[(2)]);
var inst_12611 = async.call(null,inst_12610);
var state_12620__$1 = state_12620;
if(cljs.core.truth_(inst_12611)){
var statearr_12625_12783 = state_12620__$1;
(statearr_12625_12783[(1)] = (5));

} else {
var statearr_12626_12784 = state_12620__$1;
(statearr_12626_12784[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12621 === (3))){
var inst_12618 = (state_12620[(2)]);
var state_12620__$1 = state_12620;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_12620__$1,inst_12618);
} else {
if((state_val_12621 === (2))){
var state_12620__$1 = state_12620;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_12620__$1,(4),jobs);
} else {
if((state_val_12621 === (1))){
var state_12620__$1 = state_12620;
var statearr_12627_12785 = state_12620__$1;
(statearr_12627_12785[(2)] = null);

(statearr_12627_12785[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
});})(__12776,c__6505__auto___12779,G__12607_12777,n__4517__auto___12775,jobs,results,process,async))
;
return ((function (__12776,switch__6490__auto__,c__6505__auto___12779,G__12607_12777,n__4517__auto___12775,jobs,results,process,async){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_12631 = [null,null,null,null,null,null,null];
(statearr_12631[(0)] = state_machine__6491__auto__);

(statearr_12631[(1)] = (1));

return statearr_12631;
});
var state_machine__6491__auto____1 = (function (state_12620){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_12620);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e12632){if((e12632 instanceof Object)){
var ex__6494__auto__ = e12632;
var statearr_12633_12786 = state_12620;
(statearr_12633_12786[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_12620);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e12632;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__12787 = state_12620;
state_12620 = G__12787;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_12620){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_12620);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(__12776,switch__6490__auto__,c__6505__auto___12779,G__12607_12777,n__4517__auto___12775,jobs,results,process,async))
})();
var state__6507__auto__ = (function (){var statearr_12634 = f__6506__auto__.call(null);
(statearr_12634[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___12779);

return statearr_12634;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(__12776,c__6505__auto___12779,G__12607_12777,n__4517__auto___12775,jobs,results,process,async))
);


break;
case "compute":
var c__6505__auto___12788 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (__12776,c__6505__auto___12788,G__12607_12777,n__4517__auto___12775,jobs,results,process,async){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (__12776,c__6505__auto___12788,G__12607_12777,n__4517__auto___12775,jobs,results,process,async){
return (function (state_12647){
var state_val_12648 = (state_12647[(1)]);
if((state_val_12648 === (7))){
var inst_12643 = (state_12647[(2)]);
var state_12647__$1 = state_12647;
var statearr_12649_12789 = state_12647__$1;
(statearr_12649_12789[(2)] = inst_12643);

(statearr_12649_12789[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12648 === (6))){
var state_12647__$1 = state_12647;
var statearr_12650_12790 = state_12647__$1;
(statearr_12650_12790[(2)] = null);

(statearr_12650_12790[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12648 === (5))){
var state_12647__$1 = state_12647;
var statearr_12651_12791 = state_12647__$1;
(statearr_12651_12791[(2)] = null);

(statearr_12651_12791[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12648 === (4))){
var inst_12637 = (state_12647[(2)]);
var inst_12638 = process.call(null,inst_12637);
var state_12647__$1 = state_12647;
if(cljs.core.truth_(inst_12638)){
var statearr_12652_12792 = state_12647__$1;
(statearr_12652_12792[(1)] = (5));

} else {
var statearr_12653_12793 = state_12647__$1;
(statearr_12653_12793[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12648 === (3))){
var inst_12645 = (state_12647[(2)]);
var state_12647__$1 = state_12647;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_12647__$1,inst_12645);
} else {
if((state_val_12648 === (2))){
var state_12647__$1 = state_12647;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_12647__$1,(4),jobs);
} else {
if((state_val_12648 === (1))){
var state_12647__$1 = state_12647;
var statearr_12654_12794 = state_12647__$1;
(statearr_12654_12794[(2)] = null);

(statearr_12654_12794[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
});})(__12776,c__6505__auto___12788,G__12607_12777,n__4517__auto___12775,jobs,results,process,async))
;
return ((function (__12776,switch__6490__auto__,c__6505__auto___12788,G__12607_12777,n__4517__auto___12775,jobs,results,process,async){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_12658 = [null,null,null,null,null,null,null];
(statearr_12658[(0)] = state_machine__6491__auto__);

(statearr_12658[(1)] = (1));

return statearr_12658;
});
var state_machine__6491__auto____1 = (function (state_12647){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_12647);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e12659){if((e12659 instanceof Object)){
var ex__6494__auto__ = e12659;
var statearr_12660_12795 = state_12647;
(statearr_12660_12795[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_12647);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e12659;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__12796 = state_12647;
state_12647 = G__12796;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_12647){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_12647);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(__12776,switch__6490__auto__,c__6505__auto___12788,G__12607_12777,n__4517__auto___12775,jobs,results,process,async))
})();
var state__6507__auto__ = (function (){var statearr_12661 = f__6506__auto__.call(null);
(statearr_12661[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___12788);

return statearr_12661;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(__12776,c__6505__auto___12788,G__12607_12777,n__4517__auto___12775,jobs,results,process,async))
);


break;
default:
throw (new Error([cljs.core.str("No matching clause: "),cljs.core.str(type)].join('')));

}

var G__12797 = (__12776 + (1));
__12776 = G__12797;
continue;
} else {
}
break;
}

var c__6505__auto___12798 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___12798,jobs,results,process,async){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___12798,jobs,results,process,async){
return (function (state_12683){
var state_val_12684 = (state_12683[(1)]);
if((state_val_12684 === (9))){
var inst_12676 = (state_12683[(2)]);
var state_12683__$1 = (function (){var statearr_12685 = state_12683;
(statearr_12685[(7)] = inst_12676);

return statearr_12685;
})();
var statearr_12686_12799 = state_12683__$1;
(statearr_12686_12799[(2)] = null);

(statearr_12686_12799[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12684 === (8))){
var inst_12669 = (state_12683[(8)]);
var inst_12674 = (state_12683[(2)]);
var state_12683__$1 = (function (){var statearr_12687 = state_12683;
(statearr_12687[(9)] = inst_12674);

return statearr_12687;
})();
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_12683__$1,(9),results,inst_12669);
} else {
if((state_val_12684 === (7))){
var inst_12679 = (state_12683[(2)]);
var state_12683__$1 = state_12683;
var statearr_12688_12800 = state_12683__$1;
(statearr_12688_12800[(2)] = inst_12679);

(statearr_12688_12800[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12684 === (6))){
var inst_12669 = (state_12683[(8)]);
var inst_12664 = (state_12683[(10)]);
var inst_12669__$1 = cljs.core.async.chan.call(null,(1));
var inst_12670 = cljs.core.PersistentVector.EMPTY_NODE;
var inst_12671 = [inst_12664,inst_12669__$1];
var inst_12672 = (new cljs.core.PersistentVector(null,2,(5),inst_12670,inst_12671,null));
var state_12683__$1 = (function (){var statearr_12689 = state_12683;
(statearr_12689[(8)] = inst_12669__$1);

return statearr_12689;
})();
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_12683__$1,(8),jobs,inst_12672);
} else {
if((state_val_12684 === (5))){
var inst_12667 = cljs.core.async.close_BANG_.call(null,jobs);
var state_12683__$1 = state_12683;
var statearr_12690_12801 = state_12683__$1;
(statearr_12690_12801[(2)] = inst_12667);

(statearr_12690_12801[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12684 === (4))){
var inst_12664 = (state_12683[(10)]);
var inst_12664__$1 = (state_12683[(2)]);
var inst_12665 = (inst_12664__$1 == null);
var state_12683__$1 = (function (){var statearr_12691 = state_12683;
(statearr_12691[(10)] = inst_12664__$1);

return statearr_12691;
})();
if(cljs.core.truth_(inst_12665)){
var statearr_12692_12802 = state_12683__$1;
(statearr_12692_12802[(1)] = (5));

} else {
var statearr_12693_12803 = state_12683__$1;
(statearr_12693_12803[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12684 === (3))){
var inst_12681 = (state_12683[(2)]);
var state_12683__$1 = state_12683;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_12683__$1,inst_12681);
} else {
if((state_val_12684 === (2))){
var state_12683__$1 = state_12683;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_12683__$1,(4),from);
} else {
if((state_val_12684 === (1))){
var state_12683__$1 = state_12683;
var statearr_12694_12804 = state_12683__$1;
(statearr_12694_12804[(2)] = null);

(statearr_12694_12804[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___12798,jobs,results,process,async))
;
return ((function (switch__6490__auto__,c__6505__auto___12798,jobs,results,process,async){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_12698 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_12698[(0)] = state_machine__6491__auto__);

(statearr_12698[(1)] = (1));

return statearr_12698;
});
var state_machine__6491__auto____1 = (function (state_12683){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_12683);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e12699){if((e12699 instanceof Object)){
var ex__6494__auto__ = e12699;
var statearr_12700_12805 = state_12683;
(statearr_12700_12805[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_12683);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e12699;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__12806 = state_12683;
state_12683 = G__12806;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_12683){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_12683);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___12798,jobs,results,process,async))
})();
var state__6507__auto__ = (function (){var statearr_12701 = f__6506__auto__.call(null);
(statearr_12701[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___12798);

return statearr_12701;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___12798,jobs,results,process,async))
);


var c__6505__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto__,jobs,results,process,async){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto__,jobs,results,process,async){
return (function (state_12739){
var state_val_12740 = (state_12739[(1)]);
if((state_val_12740 === (7))){
var inst_12735 = (state_12739[(2)]);
var state_12739__$1 = state_12739;
var statearr_12741_12807 = state_12739__$1;
(statearr_12741_12807[(2)] = inst_12735);

(statearr_12741_12807[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (20))){
var state_12739__$1 = state_12739;
var statearr_12742_12808 = state_12739__$1;
(statearr_12742_12808[(2)] = null);

(statearr_12742_12808[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (1))){
var state_12739__$1 = state_12739;
var statearr_12743_12809 = state_12739__$1;
(statearr_12743_12809[(2)] = null);

(statearr_12743_12809[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (4))){
var inst_12704 = (state_12739[(7)]);
var inst_12704__$1 = (state_12739[(2)]);
var inst_12705 = (inst_12704__$1 == null);
var state_12739__$1 = (function (){var statearr_12744 = state_12739;
(statearr_12744[(7)] = inst_12704__$1);

return statearr_12744;
})();
if(cljs.core.truth_(inst_12705)){
var statearr_12745_12810 = state_12739__$1;
(statearr_12745_12810[(1)] = (5));

} else {
var statearr_12746_12811 = state_12739__$1;
(statearr_12746_12811[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (15))){
var inst_12717 = (state_12739[(8)]);
var state_12739__$1 = state_12739;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_12739__$1,(18),to,inst_12717);
} else {
if((state_val_12740 === (21))){
var inst_12730 = (state_12739[(2)]);
var state_12739__$1 = state_12739;
var statearr_12747_12812 = state_12739__$1;
(statearr_12747_12812[(2)] = inst_12730);

(statearr_12747_12812[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (13))){
var inst_12732 = (state_12739[(2)]);
var state_12739__$1 = (function (){var statearr_12748 = state_12739;
(statearr_12748[(9)] = inst_12732);

return statearr_12748;
})();
var statearr_12749_12813 = state_12739__$1;
(statearr_12749_12813[(2)] = null);

(statearr_12749_12813[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (6))){
var inst_12704 = (state_12739[(7)]);
var state_12739__$1 = state_12739;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_12739__$1,(11),inst_12704);
} else {
if((state_val_12740 === (17))){
var inst_12725 = (state_12739[(2)]);
var state_12739__$1 = state_12739;
if(cljs.core.truth_(inst_12725)){
var statearr_12750_12814 = state_12739__$1;
(statearr_12750_12814[(1)] = (19));

} else {
var statearr_12751_12815 = state_12739__$1;
(statearr_12751_12815[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (3))){
var inst_12737 = (state_12739[(2)]);
var state_12739__$1 = state_12739;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_12739__$1,inst_12737);
} else {
if((state_val_12740 === (12))){
var inst_12714 = (state_12739[(10)]);
var state_12739__$1 = state_12739;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_12739__$1,(14),inst_12714);
} else {
if((state_val_12740 === (2))){
var state_12739__$1 = state_12739;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_12739__$1,(4),results);
} else {
if((state_val_12740 === (19))){
var state_12739__$1 = state_12739;
var statearr_12752_12816 = state_12739__$1;
(statearr_12752_12816[(2)] = null);

(statearr_12752_12816[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (11))){
var inst_12714 = (state_12739[(2)]);
var state_12739__$1 = (function (){var statearr_12753 = state_12739;
(statearr_12753[(10)] = inst_12714);

return statearr_12753;
})();
var statearr_12754_12817 = state_12739__$1;
(statearr_12754_12817[(2)] = null);

(statearr_12754_12817[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (9))){
var state_12739__$1 = state_12739;
var statearr_12755_12818 = state_12739__$1;
(statearr_12755_12818[(2)] = null);

(statearr_12755_12818[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (5))){
var state_12739__$1 = state_12739;
if(cljs.core.truth_(close_QMARK_)){
var statearr_12756_12819 = state_12739__$1;
(statearr_12756_12819[(1)] = (8));

} else {
var statearr_12757_12820 = state_12739__$1;
(statearr_12757_12820[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (14))){
var inst_12717 = (state_12739[(8)]);
var inst_12719 = (state_12739[(11)]);
var inst_12717__$1 = (state_12739[(2)]);
var inst_12718 = (inst_12717__$1 == null);
var inst_12719__$1 = cljs.core.not.call(null,inst_12718);
var state_12739__$1 = (function (){var statearr_12758 = state_12739;
(statearr_12758[(8)] = inst_12717__$1);

(statearr_12758[(11)] = inst_12719__$1);

return statearr_12758;
})();
if(inst_12719__$1){
var statearr_12759_12821 = state_12739__$1;
(statearr_12759_12821[(1)] = (15));

} else {
var statearr_12760_12822 = state_12739__$1;
(statearr_12760_12822[(1)] = (16));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (16))){
var inst_12719 = (state_12739[(11)]);
var state_12739__$1 = state_12739;
var statearr_12761_12823 = state_12739__$1;
(statearr_12761_12823[(2)] = inst_12719);

(statearr_12761_12823[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (10))){
var inst_12711 = (state_12739[(2)]);
var state_12739__$1 = state_12739;
var statearr_12762_12824 = state_12739__$1;
(statearr_12762_12824[(2)] = inst_12711);

(statearr_12762_12824[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (18))){
var inst_12722 = (state_12739[(2)]);
var state_12739__$1 = state_12739;
var statearr_12763_12825 = state_12739__$1;
(statearr_12763_12825[(2)] = inst_12722);

(statearr_12763_12825[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12740 === (8))){
var inst_12708 = cljs.core.async.close_BANG_.call(null,to);
var state_12739__$1 = state_12739;
var statearr_12764_12826 = state_12739__$1;
(statearr_12764_12826[(2)] = inst_12708);

(statearr_12764_12826[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto__,jobs,results,process,async))
;
return ((function (switch__6490__auto__,c__6505__auto__,jobs,results,process,async){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_12768 = [null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_12768[(0)] = state_machine__6491__auto__);

(statearr_12768[(1)] = (1));

return statearr_12768;
});
var state_machine__6491__auto____1 = (function (state_12739){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_12739);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e12769){if((e12769 instanceof Object)){
var ex__6494__auto__ = e12769;
var statearr_12770_12827 = state_12739;
(statearr_12770_12827[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_12739);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e12769;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__12828 = state_12739;
state_12739 = G__12828;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_12739){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_12739);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto__,jobs,results,process,async))
})();
var state__6507__auto__ = (function (){var statearr_12771 = f__6506__auto__.call(null);
(statearr_12771[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto__);

return statearr_12771;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto__,jobs,results,process,async))
);

return c__6505__auto__;
});
/**
* Takes elements from the from channel and supplies them to the to
* channel, subject to the async function af, with parallelism n. af
* must be a function of two arguments, the first an input value and
* the second a channel on which to place the result(s). af must close!
* the channel before returning.  The presumption is that af will
* return immediately, having launched some asynchronous operation
* whose completion/callback will manipulate the result channel. Outputs
* will be returned in order relative to  the inputs. By default, the to
* channel will be closed when the from channel closes, but can be
* determined by the close?  parameter. Will stop consuming the from
* channel if the to channel closes.
*/
cljs.core.async.pipeline_async = (function() {
var pipeline_async = null;
var pipeline_async__4 = (function (n,to,af,from){
return pipeline_async.call(null,n,to,af,from,true);
});
var pipeline_async__5 = (function (n,to,af,from,close_QMARK_){
return cljs.core.async.pipeline_STAR_.call(null,n,to,af,from,close_QMARK_,null,new cljs.core.Keyword(null,"async","async",1050769601));
});
pipeline_async = function(n,to,af,from,close_QMARK_){
switch(arguments.length){
case 4:
return pipeline_async__4.call(this,n,to,af,from);
case 5:
return pipeline_async__5.call(this,n,to,af,from,close_QMARK_);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
pipeline_async.cljs$core$IFn$_invoke$arity$4 = pipeline_async__4;
pipeline_async.cljs$core$IFn$_invoke$arity$5 = pipeline_async__5;
return pipeline_async;
})()
;
/**
* Takes elements from the from channel and supplies them to the to
* channel, subject to the transducer xf, with parallelism n. Because
* it is parallel, the transducer will be applied independently to each
* element, not across elements, and may produce zero or more outputs
* per input.  Outputs will be returned in order relative to the
* inputs. By default, the to channel will be closed when the from
* channel closes, but can be determined by the close?  parameter. Will
* stop consuming the from channel if the to channel closes.
* 
* Note this is supplied for API compatibility with the Clojure version.
* Values of N > 1 will not result in actual concurrency in a
* single-threaded runtime.
*/
cljs.core.async.pipeline = (function() {
var pipeline = null;
var pipeline__4 = (function (n,to,xf,from){
return pipeline.call(null,n,to,xf,from,true);
});
var pipeline__5 = (function (n,to,xf,from,close_QMARK_){
return pipeline.call(null,n,to,xf,from,close_QMARK_,null);
});
var pipeline__6 = (function (n,to,xf,from,close_QMARK_,ex_handler){
return cljs.core.async.pipeline_STAR_.call(null,n,to,xf,from,close_QMARK_,ex_handler,new cljs.core.Keyword(null,"compute","compute",1555393130));
});
pipeline = function(n,to,xf,from,close_QMARK_,ex_handler){
switch(arguments.length){
case 4:
return pipeline__4.call(this,n,to,xf,from);
case 5:
return pipeline__5.call(this,n,to,xf,from,close_QMARK_);
case 6:
return pipeline__6.call(this,n,to,xf,from,close_QMARK_,ex_handler);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
pipeline.cljs$core$IFn$_invoke$arity$4 = pipeline__4;
pipeline.cljs$core$IFn$_invoke$arity$5 = pipeline__5;
pipeline.cljs$core$IFn$_invoke$arity$6 = pipeline__6;
return pipeline;
})()
;
/**
* Takes a predicate and a source channel and returns a vector of two
* channels, the first of which will contain the values for which the
* predicate returned true, the second those for which it returned
* false.
* 
* The out channels will be unbuffered by default, or two buf-or-ns can
* be supplied. The channels will close after the source channel has
* closed.
*/
cljs.core.async.split = (function() {
var split = null;
var split__2 = (function (p,ch){
return split.call(null,p,ch,null,null);
});
var split__4 = (function (p,ch,t_buf_or_n,f_buf_or_n){
var tc = cljs.core.async.chan.call(null,t_buf_or_n);
var fc = cljs.core.async.chan.call(null,f_buf_or_n);
var c__6505__auto___12929 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___12929,tc,fc){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___12929,tc,fc){
return (function (state_12904){
var state_val_12905 = (state_12904[(1)]);
if((state_val_12905 === (7))){
var inst_12900 = (state_12904[(2)]);
var state_12904__$1 = state_12904;
var statearr_12906_12930 = state_12904__$1;
(statearr_12906_12930[(2)] = inst_12900);

(statearr_12906_12930[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12905 === (1))){
var state_12904__$1 = state_12904;
var statearr_12907_12931 = state_12904__$1;
(statearr_12907_12931[(2)] = null);

(statearr_12907_12931[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12905 === (4))){
var inst_12881 = (state_12904[(7)]);
var inst_12881__$1 = (state_12904[(2)]);
var inst_12882 = (inst_12881__$1 == null);
var state_12904__$1 = (function (){var statearr_12908 = state_12904;
(statearr_12908[(7)] = inst_12881__$1);

return statearr_12908;
})();
if(cljs.core.truth_(inst_12882)){
var statearr_12909_12932 = state_12904__$1;
(statearr_12909_12932[(1)] = (5));

} else {
var statearr_12910_12933 = state_12904__$1;
(statearr_12910_12933[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12905 === (13))){
var state_12904__$1 = state_12904;
var statearr_12911_12934 = state_12904__$1;
(statearr_12911_12934[(2)] = null);

(statearr_12911_12934[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12905 === (6))){
var inst_12881 = (state_12904[(7)]);
var inst_12887 = p.call(null,inst_12881);
var state_12904__$1 = state_12904;
if(cljs.core.truth_(inst_12887)){
var statearr_12912_12935 = state_12904__$1;
(statearr_12912_12935[(1)] = (9));

} else {
var statearr_12913_12936 = state_12904__$1;
(statearr_12913_12936[(1)] = (10));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12905 === (3))){
var inst_12902 = (state_12904[(2)]);
var state_12904__$1 = state_12904;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_12904__$1,inst_12902);
} else {
if((state_val_12905 === (12))){
var state_12904__$1 = state_12904;
var statearr_12914_12937 = state_12904__$1;
(statearr_12914_12937[(2)] = null);

(statearr_12914_12937[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12905 === (2))){
var state_12904__$1 = state_12904;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_12904__$1,(4),ch);
} else {
if((state_val_12905 === (11))){
var inst_12881 = (state_12904[(7)]);
var inst_12891 = (state_12904[(2)]);
var state_12904__$1 = state_12904;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_12904__$1,(8),inst_12891,inst_12881);
} else {
if((state_val_12905 === (9))){
var state_12904__$1 = state_12904;
var statearr_12915_12938 = state_12904__$1;
(statearr_12915_12938[(2)] = tc);

(statearr_12915_12938[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12905 === (5))){
var inst_12884 = cljs.core.async.close_BANG_.call(null,tc);
var inst_12885 = cljs.core.async.close_BANG_.call(null,fc);
var state_12904__$1 = (function (){var statearr_12916 = state_12904;
(statearr_12916[(8)] = inst_12884);

return statearr_12916;
})();
var statearr_12917_12939 = state_12904__$1;
(statearr_12917_12939[(2)] = inst_12885);

(statearr_12917_12939[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12905 === (14))){
var inst_12898 = (state_12904[(2)]);
var state_12904__$1 = state_12904;
var statearr_12918_12940 = state_12904__$1;
(statearr_12918_12940[(2)] = inst_12898);

(statearr_12918_12940[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12905 === (10))){
var state_12904__$1 = state_12904;
var statearr_12919_12941 = state_12904__$1;
(statearr_12919_12941[(2)] = fc);

(statearr_12919_12941[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12905 === (8))){
var inst_12893 = (state_12904[(2)]);
var state_12904__$1 = state_12904;
if(cljs.core.truth_(inst_12893)){
var statearr_12920_12942 = state_12904__$1;
(statearr_12920_12942[(1)] = (12));

} else {
var statearr_12921_12943 = state_12904__$1;
(statearr_12921_12943[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___12929,tc,fc))
;
return ((function (switch__6490__auto__,c__6505__auto___12929,tc,fc){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_12925 = [null,null,null,null,null,null,null,null,null];
(statearr_12925[(0)] = state_machine__6491__auto__);

(statearr_12925[(1)] = (1));

return statearr_12925;
});
var state_machine__6491__auto____1 = (function (state_12904){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_12904);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e12926){if((e12926 instanceof Object)){
var ex__6494__auto__ = e12926;
var statearr_12927_12944 = state_12904;
(statearr_12927_12944[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_12904);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e12926;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__12945 = state_12904;
state_12904 = G__12945;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_12904){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_12904);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___12929,tc,fc))
})();
var state__6507__auto__ = (function (){var statearr_12928 = f__6506__auto__.call(null);
(statearr_12928[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___12929);

return statearr_12928;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___12929,tc,fc))
);


return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tc,fc], null);
});
split = function(p,ch,t_buf_or_n,f_buf_or_n){
switch(arguments.length){
case 2:
return split__2.call(this,p,ch);
case 4:
return split__4.call(this,p,ch,t_buf_or_n,f_buf_or_n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
split.cljs$core$IFn$_invoke$arity$2 = split__2;
split.cljs$core$IFn$_invoke$arity$4 = split__4;
return split;
})()
;
/**
* f should be a function of 2 arguments. Returns a channel containing
* the single result of applying f to init and the first item from the
* channel, then applying f to that result and the 2nd item, etc. If
* the channel closes without yielding items, returns init and f is not
* called. ch must close before reduce produces a result.
*/
cljs.core.async.reduce = (function reduce(f,init,ch){
var c__6505__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto__){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto__){
return (function (state_12992){
var state_val_12993 = (state_12992[(1)]);
if((state_val_12993 === (7))){
var inst_12988 = (state_12992[(2)]);
var state_12992__$1 = state_12992;
var statearr_12994_13010 = state_12992__$1;
(statearr_12994_13010[(2)] = inst_12988);

(statearr_12994_13010[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12993 === (6))){
var inst_12981 = (state_12992[(7)]);
var inst_12978 = (state_12992[(8)]);
var inst_12985 = f.call(null,inst_12978,inst_12981);
var inst_12978__$1 = inst_12985;
var state_12992__$1 = (function (){var statearr_12995 = state_12992;
(statearr_12995[(8)] = inst_12978__$1);

return statearr_12995;
})();
var statearr_12996_13011 = state_12992__$1;
(statearr_12996_13011[(2)] = null);

(statearr_12996_13011[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12993 === (5))){
var inst_12978 = (state_12992[(8)]);
var state_12992__$1 = state_12992;
var statearr_12997_13012 = state_12992__$1;
(statearr_12997_13012[(2)] = inst_12978);

(statearr_12997_13012[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12993 === (4))){
var inst_12981 = (state_12992[(7)]);
var inst_12981__$1 = (state_12992[(2)]);
var inst_12982 = (inst_12981__$1 == null);
var state_12992__$1 = (function (){var statearr_12998 = state_12992;
(statearr_12998[(7)] = inst_12981__$1);

return statearr_12998;
})();
if(cljs.core.truth_(inst_12982)){
var statearr_12999_13013 = state_12992__$1;
(statearr_12999_13013[(1)] = (5));

} else {
var statearr_13000_13014 = state_12992__$1;
(statearr_13000_13014[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_12993 === (3))){
var inst_12990 = (state_12992[(2)]);
var state_12992__$1 = state_12992;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_12992__$1,inst_12990);
} else {
if((state_val_12993 === (2))){
var state_12992__$1 = state_12992;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_12992__$1,(4),ch);
} else {
if((state_val_12993 === (1))){
var inst_12978 = init;
var state_12992__$1 = (function (){var statearr_13001 = state_12992;
(statearr_13001[(8)] = inst_12978);

return statearr_13001;
})();
var statearr_13002_13015 = state_12992__$1;
(statearr_13002_13015[(2)] = null);

(statearr_13002_13015[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
});})(c__6505__auto__))
;
return ((function (switch__6490__auto__,c__6505__auto__){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_13006 = [null,null,null,null,null,null,null,null,null];
(statearr_13006[(0)] = state_machine__6491__auto__);

(statearr_13006[(1)] = (1));

return statearr_13006;
});
var state_machine__6491__auto____1 = (function (state_12992){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_12992);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e13007){if((e13007 instanceof Object)){
var ex__6494__auto__ = e13007;
var statearr_13008_13016 = state_12992;
(statearr_13008_13016[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_12992);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e13007;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__13017 = state_12992;
state_12992 = G__13017;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_12992){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_12992);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto__))
})();
var state__6507__auto__ = (function (){var statearr_13009 = f__6506__auto__.call(null);
(statearr_13009[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto__);

return statearr_13009;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto__))
);

return c__6505__auto__;
});
/**
* Puts the contents of coll into the supplied channel.
* 
* By default the channel will be closed after the items are copied,
* but can be determined by the close? parameter.
* 
* Returns a channel which will close after the items are copied.
*/
cljs.core.async.onto_chan = (function() {
var onto_chan = null;
var onto_chan__2 = (function (ch,coll){
return onto_chan.call(null,ch,coll,true);
});
var onto_chan__3 = (function (ch,coll,close_QMARK_){
var c__6505__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto__){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto__){
return (function (state_13091){
var state_val_13092 = (state_13091[(1)]);
if((state_val_13092 === (7))){
var inst_13073 = (state_13091[(2)]);
var state_13091__$1 = state_13091;
var statearr_13093_13116 = state_13091__$1;
(statearr_13093_13116[(2)] = inst_13073);

(statearr_13093_13116[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13092 === (1))){
var inst_13067 = cljs.core.seq.call(null,coll);
var inst_13068 = inst_13067;
var state_13091__$1 = (function (){var statearr_13094 = state_13091;
(statearr_13094[(7)] = inst_13068);

return statearr_13094;
})();
var statearr_13095_13117 = state_13091__$1;
(statearr_13095_13117[(2)] = null);

(statearr_13095_13117[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13092 === (4))){
var inst_13068 = (state_13091[(7)]);
var inst_13071 = cljs.core.first.call(null,inst_13068);
var state_13091__$1 = state_13091;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_13091__$1,(7),ch,inst_13071);
} else {
if((state_val_13092 === (13))){
var inst_13085 = (state_13091[(2)]);
var state_13091__$1 = state_13091;
var statearr_13096_13118 = state_13091__$1;
(statearr_13096_13118[(2)] = inst_13085);

(statearr_13096_13118[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13092 === (6))){
var inst_13076 = (state_13091[(2)]);
var state_13091__$1 = state_13091;
if(cljs.core.truth_(inst_13076)){
var statearr_13097_13119 = state_13091__$1;
(statearr_13097_13119[(1)] = (8));

} else {
var statearr_13098_13120 = state_13091__$1;
(statearr_13098_13120[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13092 === (3))){
var inst_13089 = (state_13091[(2)]);
var state_13091__$1 = state_13091;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_13091__$1,inst_13089);
} else {
if((state_val_13092 === (12))){
var state_13091__$1 = state_13091;
var statearr_13099_13121 = state_13091__$1;
(statearr_13099_13121[(2)] = null);

(statearr_13099_13121[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13092 === (2))){
var inst_13068 = (state_13091[(7)]);
var state_13091__$1 = state_13091;
if(cljs.core.truth_(inst_13068)){
var statearr_13100_13122 = state_13091__$1;
(statearr_13100_13122[(1)] = (4));

} else {
var statearr_13101_13123 = state_13091__$1;
(statearr_13101_13123[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13092 === (11))){
var inst_13082 = cljs.core.async.close_BANG_.call(null,ch);
var state_13091__$1 = state_13091;
var statearr_13102_13124 = state_13091__$1;
(statearr_13102_13124[(2)] = inst_13082);

(statearr_13102_13124[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13092 === (9))){
var state_13091__$1 = state_13091;
if(cljs.core.truth_(close_QMARK_)){
var statearr_13103_13125 = state_13091__$1;
(statearr_13103_13125[(1)] = (11));

} else {
var statearr_13104_13126 = state_13091__$1;
(statearr_13104_13126[(1)] = (12));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13092 === (5))){
var inst_13068 = (state_13091[(7)]);
var state_13091__$1 = state_13091;
var statearr_13105_13127 = state_13091__$1;
(statearr_13105_13127[(2)] = inst_13068);

(statearr_13105_13127[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13092 === (10))){
var inst_13087 = (state_13091[(2)]);
var state_13091__$1 = state_13091;
var statearr_13106_13128 = state_13091__$1;
(statearr_13106_13128[(2)] = inst_13087);

(statearr_13106_13128[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13092 === (8))){
var inst_13068 = (state_13091[(7)]);
var inst_13078 = cljs.core.next.call(null,inst_13068);
var inst_13068__$1 = inst_13078;
var state_13091__$1 = (function (){var statearr_13107 = state_13091;
(statearr_13107[(7)] = inst_13068__$1);

return statearr_13107;
})();
var statearr_13108_13129 = state_13091__$1;
(statearr_13108_13129[(2)] = null);

(statearr_13108_13129[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto__))
;
return ((function (switch__6490__auto__,c__6505__auto__){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_13112 = [null,null,null,null,null,null,null,null];
(statearr_13112[(0)] = state_machine__6491__auto__);

(statearr_13112[(1)] = (1));

return statearr_13112;
});
var state_machine__6491__auto____1 = (function (state_13091){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_13091);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e13113){if((e13113 instanceof Object)){
var ex__6494__auto__ = e13113;
var statearr_13114_13130 = state_13091;
(statearr_13114_13130[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_13091);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e13113;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__13131 = state_13091;
state_13091 = G__13131;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_13091){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_13091);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto__))
})();
var state__6507__auto__ = (function (){var statearr_13115 = f__6506__auto__.call(null);
(statearr_13115[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto__);

return statearr_13115;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto__))
);

return c__6505__auto__;
});
onto_chan = function(ch,coll,close_QMARK_){
switch(arguments.length){
case 2:
return onto_chan__2.call(this,ch,coll);
case 3:
return onto_chan__3.call(this,ch,coll,close_QMARK_);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
onto_chan.cljs$core$IFn$_invoke$arity$2 = onto_chan__2;
onto_chan.cljs$core$IFn$_invoke$arity$3 = onto_chan__3;
return onto_chan;
})()
;
/**
* Creates and returns a channel which contains the contents of coll,
* closing when exhausted.
*/
cljs.core.async.to_chan = (function to_chan(coll){
var ch = cljs.core.async.chan.call(null,cljs.core.bounded_count.call(null,(100),coll));
cljs.core.async.onto_chan.call(null,ch,coll);

return ch;
});

cljs.core.async.Mux = (function (){var obj13133 = {};
return obj13133;
})();

cljs.core.async.muxch_STAR_ = (function muxch_STAR_(_){
if((function (){var and__3618__auto__ = _;
if(and__3618__auto__){
return _.cljs$core$async$Mux$muxch_STAR_$arity$1;
} else {
return and__3618__auto__;
}
})()){
return _.cljs$core$async$Mux$muxch_STAR_$arity$1(_);
} else {
var x__4274__auto__ = (((_ == null))?null:_);
return (function (){var or__3630__auto__ = (cljs.core.async.muxch_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.muxch_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Mux.muxch*",_);
}
}
})().call(null,_);
}
});


cljs.core.async.Mult = (function (){var obj13135 = {};
return obj13135;
})();

cljs.core.async.tap_STAR_ = (function tap_STAR_(m,ch,close_QMARK_){
if((function (){var and__3618__auto__ = m;
if(and__3618__auto__){
return m.cljs$core$async$Mult$tap_STAR_$arity$3;
} else {
return and__3618__auto__;
}
})()){
return m.cljs$core$async$Mult$tap_STAR_$arity$3(m,ch,close_QMARK_);
} else {
var x__4274__auto__ = (((m == null))?null:m);
return (function (){var or__3630__auto__ = (cljs.core.async.tap_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.tap_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Mult.tap*",m);
}
}
})().call(null,m,ch,close_QMARK_);
}
});

cljs.core.async.untap_STAR_ = (function untap_STAR_(m,ch){
if((function (){var and__3618__auto__ = m;
if(and__3618__auto__){
return m.cljs$core$async$Mult$untap_STAR_$arity$2;
} else {
return and__3618__auto__;
}
})()){
return m.cljs$core$async$Mult$untap_STAR_$arity$2(m,ch);
} else {
var x__4274__auto__ = (((m == null))?null:m);
return (function (){var or__3630__auto__ = (cljs.core.async.untap_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.untap_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Mult.untap*",m);
}
}
})().call(null,m,ch);
}
});

cljs.core.async.untap_all_STAR_ = (function untap_all_STAR_(m){
if((function (){var and__3618__auto__ = m;
if(and__3618__auto__){
return m.cljs$core$async$Mult$untap_all_STAR_$arity$1;
} else {
return and__3618__auto__;
}
})()){
return m.cljs$core$async$Mult$untap_all_STAR_$arity$1(m);
} else {
var x__4274__auto__ = (((m == null))?null:m);
return (function (){var or__3630__auto__ = (cljs.core.async.untap_all_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.untap_all_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Mult.untap-all*",m);
}
}
})().call(null,m);
}
});

/**
* Creates and returns a mult(iple) of the supplied channel. Channels
* containing copies of the channel can be created with 'tap', and
* detached with 'untap'.
* 
* Each item is distributed to all taps in parallel and synchronously,
* i.e. each tap must accept before the next item is distributed. Use
* buffering/windowing to prevent slow taps from holding up the mult.
* 
* Items received when there are no taps get dropped.
* 
* If a tap puts to a closed channel, it will be removed from the mult.
*/
cljs.core.async.mult = (function mult(ch){
var cs = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var m = (function (){
if(typeof cljs.core.async.t13357 !== 'undefined'){
} else {

/**
* @constructor
*/
cljs.core.async.t13357 = (function (cs,ch,mult,meta13358){
this.cs = cs;
this.ch = ch;
this.mult = mult;
this.meta13358 = meta13358;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
cljs.core.async.t13357.prototype.cljs$core$async$Mult$ = true;

cljs.core.async.t13357.prototype.cljs$core$async$Mult$tap_STAR_$arity$3 = ((function (cs){
return (function (_,ch__$1,close_QMARK_){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.call(null,self__.cs,cljs.core.assoc,ch__$1,close_QMARK_);

return null;
});})(cs))
;

cljs.core.async.t13357.prototype.cljs$core$async$Mult$untap_STAR_$arity$2 = ((function (cs){
return (function (_,ch__$1){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.call(null,self__.cs,cljs.core.dissoc,ch__$1);

return null;
});})(cs))
;

cljs.core.async.t13357.prototype.cljs$core$async$Mult$untap_all_STAR_$arity$1 = ((function (cs){
return (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_.call(null,self__.cs,cljs.core.PersistentArrayMap.EMPTY);

return null;
});})(cs))
;

cljs.core.async.t13357.prototype.cljs$core$async$Mux$ = true;

cljs.core.async.t13357.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = ((function (cs){
return (function (_){
var self__ = this;
var ___$1 = this;
return self__.ch;
});})(cs))
;

cljs.core.async.t13357.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (cs){
return (function (_13359){
var self__ = this;
var _13359__$1 = this;
return self__.meta13358;
});})(cs))
;

cljs.core.async.t13357.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (cs){
return (function (_13359,meta13358__$1){
var self__ = this;
var _13359__$1 = this;
return (new cljs.core.async.t13357(self__.cs,self__.ch,self__.mult,meta13358__$1));
});})(cs))
;

cljs.core.async.t13357.cljs$lang$type = true;

cljs.core.async.t13357.cljs$lang$ctorStr = "cljs.core.async/t13357";

cljs.core.async.t13357.cljs$lang$ctorPrWriter = ((function (cs){
return (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"cljs.core.async/t13357");
});})(cs))
;

cljs.core.async.__GT_t13357 = ((function (cs){
return (function __GT_t13357(cs__$1,ch__$1,mult__$1,meta13358){
return (new cljs.core.async.t13357(cs__$1,ch__$1,mult__$1,meta13358));
});})(cs))
;

}

return (new cljs.core.async.t13357(cs,ch,mult,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"line","line",212345235),390,new cljs.core.Keyword(null,"column","column",2078222095),11,new cljs.core.Keyword(null,"end-line","end-line",1837326455),397,new cljs.core.Keyword(null,"end-column","end-column",1425389514),48], null)));
})()
;
var dchan = cljs.core.async.chan.call(null,(1));
var dctr = cljs.core.atom.call(null,null);
var done = ((function (cs,m,dchan,dctr){
return (function (_){
if((cljs.core.swap_BANG_.call(null,dctr,cljs.core.dec) === (0))){
return cljs.core.async.put_BANG_.call(null,dchan,true);
} else {
return null;
}
});})(cs,m,dchan,dctr))
;
var c__6505__auto___13578 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___13578,cs,m,dchan,dctr,done){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___13578,cs,m,dchan,dctr,done){
return (function (state_13490){
var state_val_13491 = (state_13490[(1)]);
if((state_val_13491 === (7))){
var inst_13486 = (state_13490[(2)]);
var state_13490__$1 = state_13490;
var statearr_13492_13579 = state_13490__$1;
(statearr_13492_13579[(2)] = inst_13486);

(statearr_13492_13579[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (20))){
var inst_13391 = (state_13490[(7)]);
var inst_13401 = cljs.core.first.call(null,inst_13391);
var inst_13402 = cljs.core.nth.call(null,inst_13401,(0),null);
var inst_13403 = cljs.core.nth.call(null,inst_13401,(1),null);
var state_13490__$1 = (function (){var statearr_13493 = state_13490;
(statearr_13493[(8)] = inst_13402);

return statearr_13493;
})();
if(cljs.core.truth_(inst_13403)){
var statearr_13494_13580 = state_13490__$1;
(statearr_13494_13580[(1)] = (22));

} else {
var statearr_13495_13581 = state_13490__$1;
(statearr_13495_13581[(1)] = (23));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (27))){
var inst_13438 = (state_13490[(9)]);
var inst_13362 = (state_13490[(10)]);
var inst_13433 = (state_13490[(11)]);
var inst_13431 = (state_13490[(12)]);
var inst_13438__$1 = cljs.core._nth.call(null,inst_13431,inst_13433);
var inst_13439 = cljs.core.async.put_BANG_.call(null,inst_13438__$1,inst_13362,done);
var state_13490__$1 = (function (){var statearr_13496 = state_13490;
(statearr_13496[(9)] = inst_13438__$1);

return statearr_13496;
})();
if(cljs.core.truth_(inst_13439)){
var statearr_13497_13582 = state_13490__$1;
(statearr_13497_13582[(1)] = (30));

} else {
var statearr_13498_13583 = state_13490__$1;
(statearr_13498_13583[(1)] = (31));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (1))){
var state_13490__$1 = state_13490;
var statearr_13499_13584 = state_13490__$1;
(statearr_13499_13584[(2)] = null);

(statearr_13499_13584[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (24))){
var inst_13391 = (state_13490[(7)]);
var inst_13408 = (state_13490[(2)]);
var inst_13409 = cljs.core.next.call(null,inst_13391);
var inst_13371 = inst_13409;
var inst_13372 = null;
var inst_13373 = (0);
var inst_13374 = (0);
var state_13490__$1 = (function (){var statearr_13500 = state_13490;
(statearr_13500[(13)] = inst_13374);

(statearr_13500[(14)] = inst_13408);

(statearr_13500[(15)] = inst_13372);

(statearr_13500[(16)] = inst_13373);

(statearr_13500[(17)] = inst_13371);

return statearr_13500;
})();
var statearr_13501_13585 = state_13490__$1;
(statearr_13501_13585[(2)] = null);

(statearr_13501_13585[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (39))){
var state_13490__$1 = state_13490;
var statearr_13505_13586 = state_13490__$1;
(statearr_13505_13586[(2)] = null);

(statearr_13505_13586[(1)] = (41));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (4))){
var inst_13362 = (state_13490[(10)]);
var inst_13362__$1 = (state_13490[(2)]);
var inst_13363 = (inst_13362__$1 == null);
var state_13490__$1 = (function (){var statearr_13506 = state_13490;
(statearr_13506[(10)] = inst_13362__$1);

return statearr_13506;
})();
if(cljs.core.truth_(inst_13363)){
var statearr_13507_13587 = state_13490__$1;
(statearr_13507_13587[(1)] = (5));

} else {
var statearr_13508_13588 = state_13490__$1;
(statearr_13508_13588[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (15))){
var inst_13374 = (state_13490[(13)]);
var inst_13372 = (state_13490[(15)]);
var inst_13373 = (state_13490[(16)]);
var inst_13371 = (state_13490[(17)]);
var inst_13387 = (state_13490[(2)]);
var inst_13388 = (inst_13374 + (1));
var tmp13502 = inst_13372;
var tmp13503 = inst_13373;
var tmp13504 = inst_13371;
var inst_13371__$1 = tmp13504;
var inst_13372__$1 = tmp13502;
var inst_13373__$1 = tmp13503;
var inst_13374__$1 = inst_13388;
var state_13490__$1 = (function (){var statearr_13509 = state_13490;
(statearr_13509[(13)] = inst_13374__$1);

(statearr_13509[(18)] = inst_13387);

(statearr_13509[(15)] = inst_13372__$1);

(statearr_13509[(16)] = inst_13373__$1);

(statearr_13509[(17)] = inst_13371__$1);

return statearr_13509;
})();
var statearr_13510_13589 = state_13490__$1;
(statearr_13510_13589[(2)] = null);

(statearr_13510_13589[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (21))){
var inst_13412 = (state_13490[(2)]);
var state_13490__$1 = state_13490;
var statearr_13514_13590 = state_13490__$1;
(statearr_13514_13590[(2)] = inst_13412);

(statearr_13514_13590[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (31))){
var inst_13438 = (state_13490[(9)]);
var inst_13442 = done.call(null,null);
var inst_13443 = cljs.core.async.untap_STAR_.call(null,m,inst_13438);
var state_13490__$1 = (function (){var statearr_13515 = state_13490;
(statearr_13515[(19)] = inst_13442);

return statearr_13515;
})();
var statearr_13516_13591 = state_13490__$1;
(statearr_13516_13591[(2)] = inst_13443);

(statearr_13516_13591[(1)] = (32));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (32))){
var inst_13432 = (state_13490[(20)]);
var inst_13430 = (state_13490[(21)]);
var inst_13433 = (state_13490[(11)]);
var inst_13431 = (state_13490[(12)]);
var inst_13445 = (state_13490[(2)]);
var inst_13446 = (inst_13433 + (1));
var tmp13511 = inst_13432;
var tmp13512 = inst_13430;
var tmp13513 = inst_13431;
var inst_13430__$1 = tmp13512;
var inst_13431__$1 = tmp13513;
var inst_13432__$1 = tmp13511;
var inst_13433__$1 = inst_13446;
var state_13490__$1 = (function (){var statearr_13517 = state_13490;
(statearr_13517[(20)] = inst_13432__$1);

(statearr_13517[(22)] = inst_13445);

(statearr_13517[(21)] = inst_13430__$1);

(statearr_13517[(11)] = inst_13433__$1);

(statearr_13517[(12)] = inst_13431__$1);

return statearr_13517;
})();
var statearr_13518_13592 = state_13490__$1;
(statearr_13518_13592[(2)] = null);

(statearr_13518_13592[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (40))){
var inst_13458 = (state_13490[(23)]);
var inst_13462 = done.call(null,null);
var inst_13463 = cljs.core.async.untap_STAR_.call(null,m,inst_13458);
var state_13490__$1 = (function (){var statearr_13519 = state_13490;
(statearr_13519[(24)] = inst_13462);

return statearr_13519;
})();
var statearr_13520_13593 = state_13490__$1;
(statearr_13520_13593[(2)] = inst_13463);

(statearr_13520_13593[(1)] = (41));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (33))){
var inst_13449 = (state_13490[(25)]);
var inst_13451 = cljs.core.chunked_seq_QMARK_.call(null,inst_13449);
var state_13490__$1 = state_13490;
if(inst_13451){
var statearr_13521_13594 = state_13490__$1;
(statearr_13521_13594[(1)] = (36));

} else {
var statearr_13522_13595 = state_13490__$1;
(statearr_13522_13595[(1)] = (37));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (13))){
var inst_13381 = (state_13490[(26)]);
var inst_13384 = cljs.core.async.close_BANG_.call(null,inst_13381);
var state_13490__$1 = state_13490;
var statearr_13523_13596 = state_13490__$1;
(statearr_13523_13596[(2)] = inst_13384);

(statearr_13523_13596[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (22))){
var inst_13402 = (state_13490[(8)]);
var inst_13405 = cljs.core.async.close_BANG_.call(null,inst_13402);
var state_13490__$1 = state_13490;
var statearr_13524_13597 = state_13490__$1;
(statearr_13524_13597[(2)] = inst_13405);

(statearr_13524_13597[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (36))){
var inst_13449 = (state_13490[(25)]);
var inst_13453 = cljs.core.chunk_first.call(null,inst_13449);
var inst_13454 = cljs.core.chunk_rest.call(null,inst_13449);
var inst_13455 = cljs.core.count.call(null,inst_13453);
var inst_13430 = inst_13454;
var inst_13431 = inst_13453;
var inst_13432 = inst_13455;
var inst_13433 = (0);
var state_13490__$1 = (function (){var statearr_13525 = state_13490;
(statearr_13525[(20)] = inst_13432);

(statearr_13525[(21)] = inst_13430);

(statearr_13525[(11)] = inst_13433);

(statearr_13525[(12)] = inst_13431);

return statearr_13525;
})();
var statearr_13526_13598 = state_13490__$1;
(statearr_13526_13598[(2)] = null);

(statearr_13526_13598[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (41))){
var inst_13449 = (state_13490[(25)]);
var inst_13465 = (state_13490[(2)]);
var inst_13466 = cljs.core.next.call(null,inst_13449);
var inst_13430 = inst_13466;
var inst_13431 = null;
var inst_13432 = (0);
var inst_13433 = (0);
var state_13490__$1 = (function (){var statearr_13527 = state_13490;
(statearr_13527[(20)] = inst_13432);

(statearr_13527[(27)] = inst_13465);

(statearr_13527[(21)] = inst_13430);

(statearr_13527[(11)] = inst_13433);

(statearr_13527[(12)] = inst_13431);

return statearr_13527;
})();
var statearr_13528_13599 = state_13490__$1;
(statearr_13528_13599[(2)] = null);

(statearr_13528_13599[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (43))){
var state_13490__$1 = state_13490;
var statearr_13529_13600 = state_13490__$1;
(statearr_13529_13600[(2)] = null);

(statearr_13529_13600[(1)] = (44));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (29))){
var inst_13474 = (state_13490[(2)]);
var state_13490__$1 = state_13490;
var statearr_13530_13601 = state_13490__$1;
(statearr_13530_13601[(2)] = inst_13474);

(statearr_13530_13601[(1)] = (26));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (44))){
var inst_13483 = (state_13490[(2)]);
var state_13490__$1 = (function (){var statearr_13531 = state_13490;
(statearr_13531[(28)] = inst_13483);

return statearr_13531;
})();
var statearr_13532_13602 = state_13490__$1;
(statearr_13532_13602[(2)] = null);

(statearr_13532_13602[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (6))){
var inst_13422 = (state_13490[(29)]);
var inst_13421 = cljs.core.deref.call(null,cs);
var inst_13422__$1 = cljs.core.keys.call(null,inst_13421);
var inst_13423 = cljs.core.count.call(null,inst_13422__$1);
var inst_13424 = cljs.core.reset_BANG_.call(null,dctr,inst_13423);
var inst_13429 = cljs.core.seq.call(null,inst_13422__$1);
var inst_13430 = inst_13429;
var inst_13431 = null;
var inst_13432 = (0);
var inst_13433 = (0);
var state_13490__$1 = (function (){var statearr_13533 = state_13490;
(statearr_13533[(20)] = inst_13432);

(statearr_13533[(30)] = inst_13424);

(statearr_13533[(21)] = inst_13430);

(statearr_13533[(11)] = inst_13433);

(statearr_13533[(12)] = inst_13431);

(statearr_13533[(29)] = inst_13422__$1);

return statearr_13533;
})();
var statearr_13534_13603 = state_13490__$1;
(statearr_13534_13603[(2)] = null);

(statearr_13534_13603[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (28))){
var inst_13449 = (state_13490[(25)]);
var inst_13430 = (state_13490[(21)]);
var inst_13449__$1 = cljs.core.seq.call(null,inst_13430);
var state_13490__$1 = (function (){var statearr_13535 = state_13490;
(statearr_13535[(25)] = inst_13449__$1);

return statearr_13535;
})();
if(inst_13449__$1){
var statearr_13536_13604 = state_13490__$1;
(statearr_13536_13604[(1)] = (33));

} else {
var statearr_13537_13605 = state_13490__$1;
(statearr_13537_13605[(1)] = (34));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (25))){
var inst_13432 = (state_13490[(20)]);
var inst_13433 = (state_13490[(11)]);
var inst_13435 = (inst_13433 < inst_13432);
var inst_13436 = inst_13435;
var state_13490__$1 = state_13490;
if(cljs.core.truth_(inst_13436)){
var statearr_13538_13606 = state_13490__$1;
(statearr_13538_13606[(1)] = (27));

} else {
var statearr_13539_13607 = state_13490__$1;
(statearr_13539_13607[(1)] = (28));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (34))){
var state_13490__$1 = state_13490;
var statearr_13540_13608 = state_13490__$1;
(statearr_13540_13608[(2)] = null);

(statearr_13540_13608[(1)] = (35));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (17))){
var state_13490__$1 = state_13490;
var statearr_13541_13609 = state_13490__$1;
(statearr_13541_13609[(2)] = null);

(statearr_13541_13609[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (3))){
var inst_13488 = (state_13490[(2)]);
var state_13490__$1 = state_13490;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_13490__$1,inst_13488);
} else {
if((state_val_13491 === (12))){
var inst_13417 = (state_13490[(2)]);
var state_13490__$1 = state_13490;
var statearr_13542_13610 = state_13490__$1;
(statearr_13542_13610[(2)] = inst_13417);

(statearr_13542_13610[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (2))){
var state_13490__$1 = state_13490;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_13490__$1,(4),ch);
} else {
if((state_val_13491 === (23))){
var state_13490__$1 = state_13490;
var statearr_13543_13611 = state_13490__$1;
(statearr_13543_13611[(2)] = null);

(statearr_13543_13611[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (35))){
var inst_13472 = (state_13490[(2)]);
var state_13490__$1 = state_13490;
var statearr_13544_13612 = state_13490__$1;
(statearr_13544_13612[(2)] = inst_13472);

(statearr_13544_13612[(1)] = (29));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (19))){
var inst_13391 = (state_13490[(7)]);
var inst_13395 = cljs.core.chunk_first.call(null,inst_13391);
var inst_13396 = cljs.core.chunk_rest.call(null,inst_13391);
var inst_13397 = cljs.core.count.call(null,inst_13395);
var inst_13371 = inst_13396;
var inst_13372 = inst_13395;
var inst_13373 = inst_13397;
var inst_13374 = (0);
var state_13490__$1 = (function (){var statearr_13545 = state_13490;
(statearr_13545[(13)] = inst_13374);

(statearr_13545[(15)] = inst_13372);

(statearr_13545[(16)] = inst_13373);

(statearr_13545[(17)] = inst_13371);

return statearr_13545;
})();
var statearr_13546_13613 = state_13490__$1;
(statearr_13546_13613[(2)] = null);

(statearr_13546_13613[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (11))){
var inst_13391 = (state_13490[(7)]);
var inst_13371 = (state_13490[(17)]);
var inst_13391__$1 = cljs.core.seq.call(null,inst_13371);
var state_13490__$1 = (function (){var statearr_13547 = state_13490;
(statearr_13547[(7)] = inst_13391__$1);

return statearr_13547;
})();
if(inst_13391__$1){
var statearr_13548_13614 = state_13490__$1;
(statearr_13548_13614[(1)] = (16));

} else {
var statearr_13549_13615 = state_13490__$1;
(statearr_13549_13615[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (9))){
var inst_13419 = (state_13490[(2)]);
var state_13490__$1 = state_13490;
var statearr_13550_13616 = state_13490__$1;
(statearr_13550_13616[(2)] = inst_13419);

(statearr_13550_13616[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (5))){
var inst_13369 = cljs.core.deref.call(null,cs);
var inst_13370 = cljs.core.seq.call(null,inst_13369);
var inst_13371 = inst_13370;
var inst_13372 = null;
var inst_13373 = (0);
var inst_13374 = (0);
var state_13490__$1 = (function (){var statearr_13551 = state_13490;
(statearr_13551[(13)] = inst_13374);

(statearr_13551[(15)] = inst_13372);

(statearr_13551[(16)] = inst_13373);

(statearr_13551[(17)] = inst_13371);

return statearr_13551;
})();
var statearr_13552_13617 = state_13490__$1;
(statearr_13552_13617[(2)] = null);

(statearr_13552_13617[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (14))){
var state_13490__$1 = state_13490;
var statearr_13553_13618 = state_13490__$1;
(statearr_13553_13618[(2)] = null);

(statearr_13553_13618[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (45))){
var inst_13480 = (state_13490[(2)]);
var state_13490__$1 = state_13490;
var statearr_13554_13619 = state_13490__$1;
(statearr_13554_13619[(2)] = inst_13480);

(statearr_13554_13619[(1)] = (44));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (26))){
var inst_13422 = (state_13490[(29)]);
var inst_13476 = (state_13490[(2)]);
var inst_13477 = cljs.core.seq.call(null,inst_13422);
var state_13490__$1 = (function (){var statearr_13555 = state_13490;
(statearr_13555[(31)] = inst_13476);

return statearr_13555;
})();
if(inst_13477){
var statearr_13556_13620 = state_13490__$1;
(statearr_13556_13620[(1)] = (42));

} else {
var statearr_13557_13621 = state_13490__$1;
(statearr_13557_13621[(1)] = (43));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (16))){
var inst_13391 = (state_13490[(7)]);
var inst_13393 = cljs.core.chunked_seq_QMARK_.call(null,inst_13391);
var state_13490__$1 = state_13490;
if(inst_13393){
var statearr_13558_13622 = state_13490__$1;
(statearr_13558_13622[(1)] = (19));

} else {
var statearr_13559_13623 = state_13490__$1;
(statearr_13559_13623[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (38))){
var inst_13469 = (state_13490[(2)]);
var state_13490__$1 = state_13490;
var statearr_13560_13624 = state_13490__$1;
(statearr_13560_13624[(2)] = inst_13469);

(statearr_13560_13624[(1)] = (35));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (30))){
var state_13490__$1 = state_13490;
var statearr_13561_13625 = state_13490__$1;
(statearr_13561_13625[(2)] = null);

(statearr_13561_13625[(1)] = (32));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (10))){
var inst_13374 = (state_13490[(13)]);
var inst_13372 = (state_13490[(15)]);
var inst_13380 = cljs.core._nth.call(null,inst_13372,inst_13374);
var inst_13381 = cljs.core.nth.call(null,inst_13380,(0),null);
var inst_13382 = cljs.core.nth.call(null,inst_13380,(1),null);
var state_13490__$1 = (function (){var statearr_13562 = state_13490;
(statearr_13562[(26)] = inst_13381);

return statearr_13562;
})();
if(cljs.core.truth_(inst_13382)){
var statearr_13563_13626 = state_13490__$1;
(statearr_13563_13626[(1)] = (13));

} else {
var statearr_13564_13627 = state_13490__$1;
(statearr_13564_13627[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (18))){
var inst_13415 = (state_13490[(2)]);
var state_13490__$1 = state_13490;
var statearr_13565_13628 = state_13490__$1;
(statearr_13565_13628[(2)] = inst_13415);

(statearr_13565_13628[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (42))){
var state_13490__$1 = state_13490;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_13490__$1,(45),dchan);
} else {
if((state_val_13491 === (37))){
var inst_13449 = (state_13490[(25)]);
var inst_13362 = (state_13490[(10)]);
var inst_13458 = (state_13490[(23)]);
var inst_13458__$1 = cljs.core.first.call(null,inst_13449);
var inst_13459 = cljs.core.async.put_BANG_.call(null,inst_13458__$1,inst_13362,done);
var state_13490__$1 = (function (){var statearr_13566 = state_13490;
(statearr_13566[(23)] = inst_13458__$1);

return statearr_13566;
})();
if(cljs.core.truth_(inst_13459)){
var statearr_13567_13629 = state_13490__$1;
(statearr_13567_13629[(1)] = (39));

} else {
var statearr_13568_13630 = state_13490__$1;
(statearr_13568_13630[(1)] = (40));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13491 === (8))){
var inst_13374 = (state_13490[(13)]);
var inst_13373 = (state_13490[(16)]);
var inst_13376 = (inst_13374 < inst_13373);
var inst_13377 = inst_13376;
var state_13490__$1 = state_13490;
if(cljs.core.truth_(inst_13377)){
var statearr_13569_13631 = state_13490__$1;
(statearr_13569_13631[(1)] = (10));

} else {
var statearr_13570_13632 = state_13490__$1;
(statearr_13570_13632[(1)] = (11));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___13578,cs,m,dchan,dctr,done))
;
return ((function (switch__6490__auto__,c__6505__auto___13578,cs,m,dchan,dctr,done){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_13574 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_13574[(0)] = state_machine__6491__auto__);

(statearr_13574[(1)] = (1));

return statearr_13574;
});
var state_machine__6491__auto____1 = (function (state_13490){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_13490);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e13575){if((e13575 instanceof Object)){
var ex__6494__auto__ = e13575;
var statearr_13576_13633 = state_13490;
(statearr_13576_13633[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_13490);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e13575;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__13634 = state_13490;
state_13490 = G__13634;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_13490){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_13490);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___13578,cs,m,dchan,dctr,done))
})();
var state__6507__auto__ = (function (){var statearr_13577 = f__6506__auto__.call(null);
(statearr_13577[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___13578);

return statearr_13577;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___13578,cs,m,dchan,dctr,done))
);


return m;
});
/**
* Copies the mult source onto the supplied channel.
* 
* By default the channel will be closed when the source closes,
* but can be determined by the close? parameter.
*/
cljs.core.async.tap = (function() {
var tap = null;
var tap__2 = (function (mult,ch){
return tap.call(null,mult,ch,true);
});
var tap__3 = (function (mult,ch,close_QMARK_){
cljs.core.async.tap_STAR_.call(null,mult,ch,close_QMARK_);

return ch;
});
tap = function(mult,ch,close_QMARK_){
switch(arguments.length){
case 2:
return tap__2.call(this,mult,ch);
case 3:
return tap__3.call(this,mult,ch,close_QMARK_);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
tap.cljs$core$IFn$_invoke$arity$2 = tap__2;
tap.cljs$core$IFn$_invoke$arity$3 = tap__3;
return tap;
})()
;
/**
* Disconnects a target channel from a mult
*/
cljs.core.async.untap = (function untap(mult,ch){
return cljs.core.async.untap_STAR_.call(null,mult,ch);
});
/**
* Disconnects all target channels from a mult
*/
cljs.core.async.untap_all = (function untap_all(mult){
return cljs.core.async.untap_all_STAR_.call(null,mult);
});

cljs.core.async.Mix = (function (){var obj13636 = {};
return obj13636;
})();

cljs.core.async.admix_STAR_ = (function admix_STAR_(m,ch){
if((function (){var and__3618__auto__ = m;
if(and__3618__auto__){
return m.cljs$core$async$Mix$admix_STAR_$arity$2;
} else {
return and__3618__auto__;
}
})()){
return m.cljs$core$async$Mix$admix_STAR_$arity$2(m,ch);
} else {
var x__4274__auto__ = (((m == null))?null:m);
return (function (){var or__3630__auto__ = (cljs.core.async.admix_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.admix_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Mix.admix*",m);
}
}
})().call(null,m,ch);
}
});

cljs.core.async.unmix_STAR_ = (function unmix_STAR_(m,ch){
if((function (){var and__3618__auto__ = m;
if(and__3618__auto__){
return m.cljs$core$async$Mix$unmix_STAR_$arity$2;
} else {
return and__3618__auto__;
}
})()){
return m.cljs$core$async$Mix$unmix_STAR_$arity$2(m,ch);
} else {
var x__4274__auto__ = (((m == null))?null:m);
return (function (){var or__3630__auto__ = (cljs.core.async.unmix_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.unmix_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Mix.unmix*",m);
}
}
})().call(null,m,ch);
}
});

cljs.core.async.unmix_all_STAR_ = (function unmix_all_STAR_(m){
if((function (){var and__3618__auto__ = m;
if(and__3618__auto__){
return m.cljs$core$async$Mix$unmix_all_STAR_$arity$1;
} else {
return and__3618__auto__;
}
})()){
return m.cljs$core$async$Mix$unmix_all_STAR_$arity$1(m);
} else {
var x__4274__auto__ = (((m == null))?null:m);
return (function (){var or__3630__auto__ = (cljs.core.async.unmix_all_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.unmix_all_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Mix.unmix-all*",m);
}
}
})().call(null,m);
}
});

cljs.core.async.toggle_STAR_ = (function toggle_STAR_(m,state_map){
if((function (){var and__3618__auto__ = m;
if(and__3618__auto__){
return m.cljs$core$async$Mix$toggle_STAR_$arity$2;
} else {
return and__3618__auto__;
}
})()){
return m.cljs$core$async$Mix$toggle_STAR_$arity$2(m,state_map);
} else {
var x__4274__auto__ = (((m == null))?null:m);
return (function (){var or__3630__auto__ = (cljs.core.async.toggle_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.toggle_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Mix.toggle*",m);
}
}
})().call(null,m,state_map);
}
});

cljs.core.async.solo_mode_STAR_ = (function solo_mode_STAR_(m,mode){
if((function (){var and__3618__auto__ = m;
if(and__3618__auto__){
return m.cljs$core$async$Mix$solo_mode_STAR_$arity$2;
} else {
return and__3618__auto__;
}
})()){
return m.cljs$core$async$Mix$solo_mode_STAR_$arity$2(m,mode);
} else {
var x__4274__auto__ = (((m == null))?null:m);
return (function (){var or__3630__auto__ = (cljs.core.async.solo_mode_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.solo_mode_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Mix.solo-mode*",m);
}
}
})().call(null,m,mode);
}
});

/**
* @param {...*} var_args
*/
cljs.core.async.ioc_alts_BANG_ = (function() { 
var ioc_alts_BANG___delegate = function (state,cont_block,ports,p__13637){
var map__13642 = p__13637;
var map__13642__$1 = ((cljs.core.seq_QMARK_.call(null,map__13642))?cljs.core.apply.call(null,cljs.core.hash_map,map__13642):map__13642);
var opts = map__13642__$1;
var statearr_13643_13646 = state;
(statearr_13643_13646[cljs.core.async.impl.ioc_helpers.STATE_IDX] = cont_block);


var temp__4126__auto__ = cljs.core.async.do_alts.call(null,((function (map__13642,map__13642__$1,opts){
return (function (val){
var statearr_13644_13647 = state;
(statearr_13644_13647[cljs.core.async.impl.ioc_helpers.VALUE_IDX] = val);


return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state);
});})(map__13642,map__13642__$1,opts))
,ports,opts);
if(cljs.core.truth_(temp__4126__auto__)){
var cb = temp__4126__auto__;
var statearr_13645_13648 = state;
(statearr_13645_13648[cljs.core.async.impl.ioc_helpers.VALUE_IDX] = cljs.core.deref.call(null,cb));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
};
var ioc_alts_BANG_ = function (state,cont_block,ports,var_args){
var p__13637 = null;
if (arguments.length > 3) {
  p__13637 = cljs.core.array_seq(Array.prototype.slice.call(arguments, 3),0);
} 
return ioc_alts_BANG___delegate.call(this,state,cont_block,ports,p__13637);};
ioc_alts_BANG_.cljs$lang$maxFixedArity = 3;
ioc_alts_BANG_.cljs$lang$applyTo = (function (arglist__13649){
var state = cljs.core.first(arglist__13649);
arglist__13649 = cljs.core.next(arglist__13649);
var cont_block = cljs.core.first(arglist__13649);
arglist__13649 = cljs.core.next(arglist__13649);
var ports = cljs.core.first(arglist__13649);
var p__13637 = cljs.core.rest(arglist__13649);
return ioc_alts_BANG___delegate(state,cont_block,ports,p__13637);
});
ioc_alts_BANG_.cljs$core$IFn$_invoke$arity$variadic = ioc_alts_BANG___delegate;
return ioc_alts_BANG_;
})()
;
/**
* Creates and returns a mix of one or more input channels which will
* be put on the supplied out channel. Input sources can be added to
* the mix with 'admix', and removed with 'unmix'. A mix supports
* soloing, muting and pausing multiple inputs atomically using
* 'toggle', and can solo using either muting or pausing as determined
* by 'solo-mode'.
* 
* Each channel can have zero or more boolean modes set via 'toggle':
* 
* :solo - when true, only this (ond other soloed) channel(s) will appear
* in the mix output channel. :mute and :pause states of soloed
* channels are ignored. If solo-mode is :mute, non-soloed
* channels are muted, if :pause, non-soloed channels are
* paused.
* 
* :mute - muted channels will have their contents consumed but not included in the mix
* :pause - paused channels will not have their contents consumed (and thus also not included in the mix)
*/
cljs.core.async.mix = (function mix(out){
var cs = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var solo_modes = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"pause","pause",-2095325672),null,new cljs.core.Keyword(null,"mute","mute",1151223646),null], null), null);
var attrs = cljs.core.conj.call(null,solo_modes,new cljs.core.Keyword(null,"solo","solo",-316350075));
var solo_mode = cljs.core.atom.call(null,new cljs.core.Keyword(null,"mute","mute",1151223646));
var change = cljs.core.async.chan.call(null);
var changed = ((function (cs,solo_modes,attrs,solo_mode,change){
return (function (){
return cljs.core.async.put_BANG_.call(null,change,true);
});})(cs,solo_modes,attrs,solo_mode,change))
;
var pick = ((function (cs,solo_modes,attrs,solo_mode,change,changed){
return (function (attr,chs){
return cljs.core.reduce_kv.call(null,((function (cs,solo_modes,attrs,solo_mode,change,changed){
return (function (ret,c,v){
if(cljs.core.truth_(attr.call(null,v))){
return cljs.core.conj.call(null,ret,c);
} else {
return ret;
}
});})(cs,solo_modes,attrs,solo_mode,change,changed))
,cljs.core.PersistentHashSet.EMPTY,chs);
});})(cs,solo_modes,attrs,solo_mode,change,changed))
;
var calc_state = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick){
return (function (){
var chs = cljs.core.deref.call(null,cs);
var mode = cljs.core.deref.call(null,solo_mode);
var solos = pick.call(null,new cljs.core.Keyword(null,"solo","solo",-316350075),chs);
var pauses = pick.call(null,new cljs.core.Keyword(null,"pause","pause",-2095325672),chs);
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"solos","solos",1441458643),solos,new cljs.core.Keyword(null,"mutes","mutes",1068806309),pick.call(null,new cljs.core.Keyword(null,"mute","mute",1151223646),chs),new cljs.core.Keyword(null,"reads","reads",-1215067361),cljs.core.conj.call(null,(((cljs.core._EQ_.call(null,mode,new cljs.core.Keyword(null,"pause","pause",-2095325672))) && (!(cljs.core.empty_QMARK_.call(null,solos))))?cljs.core.vec.call(null,solos):cljs.core.vec.call(null,cljs.core.remove.call(null,pauses,cljs.core.keys.call(null,chs)))),change)], null);
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick))
;
var m = (function (){
if(typeof cljs.core.async.t13769 !== 'undefined'){
} else {

/**
* @constructor
*/
cljs.core.async.t13769 = (function (change,mix,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,meta13770){
this.change = change;
this.mix = mix;
this.solo_mode = solo_mode;
this.pick = pick;
this.cs = cs;
this.calc_state = calc_state;
this.out = out;
this.changed = changed;
this.solo_modes = solo_modes;
this.attrs = attrs;
this.meta13770 = meta13770;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
cljs.core.async.t13769.prototype.cljs$core$async$Mix$ = true;

cljs.core.async.t13769.prototype.cljs$core$async$Mix$admix_STAR_$arity$2 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_,ch){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.call(null,self__.cs,cljs.core.assoc,ch,cljs.core.PersistentArrayMap.EMPTY);

return self__.changed.call(null);
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t13769.prototype.cljs$core$async$Mix$unmix_STAR_$arity$2 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_,ch){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.call(null,self__.cs,cljs.core.dissoc,ch);

return self__.changed.call(null);
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t13769.prototype.cljs$core$async$Mix$unmix_all_STAR_$arity$1 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_.call(null,self__.cs,cljs.core.PersistentArrayMap.EMPTY);

return self__.changed.call(null);
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t13769.prototype.cljs$core$async$Mix$toggle_STAR_$arity$2 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_,state_map){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.call(null,self__.cs,cljs.core.partial.call(null,cljs.core.merge_with,cljs.core.merge),state_map);

return self__.changed.call(null);
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t13769.prototype.cljs$core$async$Mix$solo_mode_STAR_$arity$2 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_,mode){
var self__ = this;
var ___$1 = this;
if(cljs.core.truth_(self__.solo_modes.call(null,mode))){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str([cljs.core.str("mode must be one of: "),cljs.core.str(self__.solo_modes)].join('')),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.call(null,cljs.core.list(new cljs.core.Symbol(null,"solo-modes","solo-modes",882180540,null),new cljs.core.Symbol(null,"mode","mode",-2000032078,null))))].join('')));
}

cljs.core.reset_BANG_.call(null,self__.solo_mode,mode);

return self__.changed.call(null);
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t13769.prototype.cljs$core$async$Mux$ = true;

cljs.core.async.t13769.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_){
var self__ = this;
var ___$1 = this;
return self__.out;
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t13769.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_13771){
var self__ = this;
var _13771__$1 = this;
return self__.meta13770;
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t13769.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_13771,meta13770__$1){
var self__ = this;
var _13771__$1 = this;
return (new cljs.core.async.t13769(self__.change,self__.mix,self__.solo_mode,self__.pick,self__.cs,self__.calc_state,self__.out,self__.changed,self__.solo_modes,self__.attrs,meta13770__$1));
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t13769.cljs$lang$type = true;

cljs.core.async.t13769.cljs$lang$ctorStr = "cljs.core.async/t13769";

cljs.core.async.t13769.cljs$lang$ctorPrWriter = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"cljs.core.async/t13769");
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.__GT_t13769 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function __GT_t13769(change__$1,mix__$1,solo_mode__$1,pick__$1,cs__$1,calc_state__$1,out__$1,changed__$1,solo_modes__$1,attrs__$1,meta13770){
return (new cljs.core.async.t13769(change__$1,mix__$1,solo_mode__$1,pick__$1,cs__$1,calc_state__$1,out__$1,changed__$1,solo_modes__$1,attrs__$1,meta13770));
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

}

return (new cljs.core.async.t13769(change,mix,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"line","line",212345235),499,new cljs.core.Keyword(null,"column","column",2078222095),11,new cljs.core.Keyword(null,"end-line","end-line",1837326455),510,new cljs.core.Keyword(null,"end-column","end-column",1425389514),25], null)));
})()
;
var c__6505__auto___13888 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___13888,cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state,m){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___13888,cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state,m){
return (function (state_13841){
var state_val_13842 = (state_13841[(1)]);
if((state_val_13842 === (7))){
var inst_13785 = (state_13841[(7)]);
var inst_13790 = cljs.core.apply.call(null,cljs.core.hash_map,inst_13785);
var state_13841__$1 = state_13841;
var statearr_13843_13889 = state_13841__$1;
(statearr_13843_13889[(2)] = inst_13790);

(statearr_13843_13889[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (20))){
var inst_13800 = (state_13841[(8)]);
var state_13841__$1 = state_13841;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_13841__$1,(23),out,inst_13800);
} else {
if((state_val_13842 === (1))){
var inst_13775 = (state_13841[(9)]);
var inst_13775__$1 = calc_state.call(null);
var inst_13776 = cljs.core.seq_QMARK_.call(null,inst_13775__$1);
var state_13841__$1 = (function (){var statearr_13844 = state_13841;
(statearr_13844[(9)] = inst_13775__$1);

return statearr_13844;
})();
if(inst_13776){
var statearr_13845_13890 = state_13841__$1;
(statearr_13845_13890[(1)] = (2));

} else {
var statearr_13846_13891 = state_13841__$1;
(statearr_13846_13891[(1)] = (3));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (24))){
var inst_13793 = (state_13841[(10)]);
var inst_13785 = inst_13793;
var state_13841__$1 = (function (){var statearr_13847 = state_13841;
(statearr_13847[(7)] = inst_13785);

return statearr_13847;
})();
var statearr_13848_13892 = state_13841__$1;
(statearr_13848_13892[(2)] = null);

(statearr_13848_13892[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (4))){
var inst_13775 = (state_13841[(9)]);
var inst_13781 = (state_13841[(2)]);
var inst_13782 = cljs.core.get.call(null,inst_13781,new cljs.core.Keyword(null,"reads","reads",-1215067361));
var inst_13783 = cljs.core.get.call(null,inst_13781,new cljs.core.Keyword(null,"mutes","mutes",1068806309));
var inst_13784 = cljs.core.get.call(null,inst_13781,new cljs.core.Keyword(null,"solos","solos",1441458643));
var inst_13785 = inst_13775;
var state_13841__$1 = (function (){var statearr_13849 = state_13841;
(statearr_13849[(11)] = inst_13782);

(statearr_13849[(7)] = inst_13785);

(statearr_13849[(12)] = inst_13783);

(statearr_13849[(13)] = inst_13784);

return statearr_13849;
})();
var statearr_13850_13893 = state_13841__$1;
(statearr_13850_13893[(2)] = null);

(statearr_13850_13893[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (15))){
var state_13841__$1 = state_13841;
var statearr_13851_13894 = state_13841__$1;
(statearr_13851_13894[(2)] = null);

(statearr_13851_13894[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (21))){
var inst_13793 = (state_13841[(10)]);
var inst_13785 = inst_13793;
var state_13841__$1 = (function (){var statearr_13852 = state_13841;
(statearr_13852[(7)] = inst_13785);

return statearr_13852;
})();
var statearr_13853_13895 = state_13841__$1;
(statearr_13853_13895[(2)] = null);

(statearr_13853_13895[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (13))){
var inst_13837 = (state_13841[(2)]);
var state_13841__$1 = state_13841;
var statearr_13854_13896 = state_13841__$1;
(statearr_13854_13896[(2)] = inst_13837);

(statearr_13854_13896[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (22))){
var inst_13835 = (state_13841[(2)]);
var state_13841__$1 = state_13841;
var statearr_13855_13897 = state_13841__$1;
(statearr_13855_13897[(2)] = inst_13835);

(statearr_13855_13897[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (6))){
var inst_13839 = (state_13841[(2)]);
var state_13841__$1 = state_13841;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_13841__$1,inst_13839);
} else {
if((state_val_13842 === (25))){
var state_13841__$1 = state_13841;
var statearr_13856_13898 = state_13841__$1;
(statearr_13856_13898[(2)] = null);

(statearr_13856_13898[(1)] = (26));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (17))){
var inst_13815 = (state_13841[(14)]);
var state_13841__$1 = state_13841;
var statearr_13857_13899 = state_13841__$1;
(statearr_13857_13899[(2)] = inst_13815);

(statearr_13857_13899[(1)] = (19));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (3))){
var inst_13775 = (state_13841[(9)]);
var state_13841__$1 = state_13841;
var statearr_13858_13900 = state_13841__$1;
(statearr_13858_13900[(2)] = inst_13775);

(statearr_13858_13900[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (12))){
var inst_13801 = (state_13841[(15)]);
var inst_13815 = (state_13841[(14)]);
var inst_13796 = (state_13841[(16)]);
var inst_13815__$1 = inst_13796.call(null,inst_13801);
var state_13841__$1 = (function (){var statearr_13859 = state_13841;
(statearr_13859[(14)] = inst_13815__$1);

return statearr_13859;
})();
if(cljs.core.truth_(inst_13815__$1)){
var statearr_13860_13901 = state_13841__$1;
(statearr_13860_13901[(1)] = (17));

} else {
var statearr_13861_13902 = state_13841__$1;
(statearr_13861_13902[(1)] = (18));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (2))){
var inst_13775 = (state_13841[(9)]);
var inst_13778 = cljs.core.apply.call(null,cljs.core.hash_map,inst_13775);
var state_13841__$1 = state_13841;
var statearr_13862_13903 = state_13841__$1;
(statearr_13862_13903[(2)] = inst_13778);

(statearr_13862_13903[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (23))){
var inst_13826 = (state_13841[(2)]);
var state_13841__$1 = state_13841;
if(cljs.core.truth_(inst_13826)){
var statearr_13863_13904 = state_13841__$1;
(statearr_13863_13904[(1)] = (24));

} else {
var statearr_13864_13905 = state_13841__$1;
(statearr_13864_13905[(1)] = (25));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (19))){
var inst_13823 = (state_13841[(2)]);
var state_13841__$1 = state_13841;
if(cljs.core.truth_(inst_13823)){
var statearr_13865_13906 = state_13841__$1;
(statearr_13865_13906[(1)] = (20));

} else {
var statearr_13866_13907 = state_13841__$1;
(statearr_13866_13907[(1)] = (21));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (11))){
var inst_13800 = (state_13841[(8)]);
var inst_13806 = (inst_13800 == null);
var state_13841__$1 = state_13841;
if(cljs.core.truth_(inst_13806)){
var statearr_13867_13908 = state_13841__$1;
(statearr_13867_13908[(1)] = (14));

} else {
var statearr_13868_13909 = state_13841__$1;
(statearr_13868_13909[(1)] = (15));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (9))){
var inst_13793 = (state_13841[(10)]);
var inst_13793__$1 = (state_13841[(2)]);
var inst_13794 = cljs.core.get.call(null,inst_13793__$1,new cljs.core.Keyword(null,"reads","reads",-1215067361));
var inst_13795 = cljs.core.get.call(null,inst_13793__$1,new cljs.core.Keyword(null,"mutes","mutes",1068806309));
var inst_13796 = cljs.core.get.call(null,inst_13793__$1,new cljs.core.Keyword(null,"solos","solos",1441458643));
var state_13841__$1 = (function (){var statearr_13869 = state_13841;
(statearr_13869[(17)] = inst_13795);

(statearr_13869[(10)] = inst_13793__$1);

(statearr_13869[(16)] = inst_13796);

return statearr_13869;
})();
return cljs.core.async.ioc_alts_BANG_.call(null,state_13841__$1,(10),inst_13794);
} else {
if((state_val_13842 === (5))){
var inst_13785 = (state_13841[(7)]);
var inst_13788 = cljs.core.seq_QMARK_.call(null,inst_13785);
var state_13841__$1 = state_13841;
if(inst_13788){
var statearr_13870_13910 = state_13841__$1;
(statearr_13870_13910[(1)] = (7));

} else {
var statearr_13871_13911 = state_13841__$1;
(statearr_13871_13911[(1)] = (8));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (14))){
var inst_13801 = (state_13841[(15)]);
var inst_13808 = cljs.core.swap_BANG_.call(null,cs,cljs.core.dissoc,inst_13801);
var state_13841__$1 = state_13841;
var statearr_13872_13912 = state_13841__$1;
(statearr_13872_13912[(2)] = inst_13808);

(statearr_13872_13912[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (26))){
var inst_13831 = (state_13841[(2)]);
var state_13841__$1 = state_13841;
var statearr_13873_13913 = state_13841__$1;
(statearr_13873_13913[(2)] = inst_13831);

(statearr_13873_13913[(1)] = (22));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (16))){
var inst_13811 = (state_13841[(2)]);
var inst_13812 = calc_state.call(null);
var inst_13785 = inst_13812;
var state_13841__$1 = (function (){var statearr_13874 = state_13841;
(statearr_13874[(7)] = inst_13785);

(statearr_13874[(18)] = inst_13811);

return statearr_13874;
})();
var statearr_13875_13914 = state_13841__$1;
(statearr_13875_13914[(2)] = null);

(statearr_13875_13914[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (10))){
var inst_13801 = (state_13841[(15)]);
var inst_13800 = (state_13841[(8)]);
var inst_13799 = (state_13841[(2)]);
var inst_13800__$1 = cljs.core.nth.call(null,inst_13799,(0),null);
var inst_13801__$1 = cljs.core.nth.call(null,inst_13799,(1),null);
var inst_13802 = (inst_13800__$1 == null);
var inst_13803 = cljs.core._EQ_.call(null,inst_13801__$1,change);
var inst_13804 = (inst_13802) || (inst_13803);
var state_13841__$1 = (function (){var statearr_13876 = state_13841;
(statearr_13876[(15)] = inst_13801__$1);

(statearr_13876[(8)] = inst_13800__$1);

return statearr_13876;
})();
if(cljs.core.truth_(inst_13804)){
var statearr_13877_13915 = state_13841__$1;
(statearr_13877_13915[(1)] = (11));

} else {
var statearr_13878_13916 = state_13841__$1;
(statearr_13878_13916[(1)] = (12));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (18))){
var inst_13801 = (state_13841[(15)]);
var inst_13795 = (state_13841[(17)]);
var inst_13796 = (state_13841[(16)]);
var inst_13818 = cljs.core.empty_QMARK_.call(null,inst_13796);
var inst_13819 = inst_13795.call(null,inst_13801);
var inst_13820 = cljs.core.not.call(null,inst_13819);
var inst_13821 = (inst_13818) && (inst_13820);
var state_13841__$1 = state_13841;
var statearr_13879_13917 = state_13841__$1;
(statearr_13879_13917[(2)] = inst_13821);

(statearr_13879_13917[(1)] = (19));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_13842 === (8))){
var inst_13785 = (state_13841[(7)]);
var state_13841__$1 = state_13841;
var statearr_13880_13918 = state_13841__$1;
(statearr_13880_13918[(2)] = inst_13785);

(statearr_13880_13918[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___13888,cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state,m))
;
return ((function (switch__6490__auto__,c__6505__auto___13888,cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state,m){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_13884 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_13884[(0)] = state_machine__6491__auto__);

(statearr_13884[(1)] = (1));

return statearr_13884;
});
var state_machine__6491__auto____1 = (function (state_13841){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_13841);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e13885){if((e13885 instanceof Object)){
var ex__6494__auto__ = e13885;
var statearr_13886_13919 = state_13841;
(statearr_13886_13919[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_13841);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e13885;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__13920 = state_13841;
state_13841 = G__13920;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_13841){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_13841);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___13888,cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state,m))
})();
var state__6507__auto__ = (function (){var statearr_13887 = f__6506__auto__.call(null);
(statearr_13887[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___13888);

return statearr_13887;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___13888,cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state,m))
);


return m;
});
/**
* Adds ch as an input to the mix
*/
cljs.core.async.admix = (function admix(mix,ch){
return cljs.core.async.admix_STAR_.call(null,mix,ch);
});
/**
* Removes ch as an input to the mix
*/
cljs.core.async.unmix = (function unmix(mix,ch){
return cljs.core.async.unmix_STAR_.call(null,mix,ch);
});
/**
* removes all inputs from the mix
*/
cljs.core.async.unmix_all = (function unmix_all(mix){
return cljs.core.async.unmix_all_STAR_.call(null,mix);
});
/**
* Atomically sets the state(s) of one or more channels in a mix. The
* state map is a map of channels -> channel-state-map. A
* channel-state-map is a map of attrs -> boolean, where attr is one or
* more of :mute, :pause or :solo. Any states supplied are merged with
* the current state.
* 
* Note that channels can be added to a mix via toggle, which can be
* used to add channels in a particular (e.g. paused) state.
*/
cljs.core.async.toggle = (function toggle(mix,state_map){
return cljs.core.async.toggle_STAR_.call(null,mix,state_map);
});
/**
* Sets the solo mode of the mix. mode must be one of :mute or :pause
*/
cljs.core.async.solo_mode = (function solo_mode(mix,mode){
return cljs.core.async.solo_mode_STAR_.call(null,mix,mode);
});

cljs.core.async.Pub = (function (){var obj13922 = {};
return obj13922;
})();

cljs.core.async.sub_STAR_ = (function sub_STAR_(p,v,ch,close_QMARK_){
if((function (){var and__3618__auto__ = p;
if(and__3618__auto__){
return p.cljs$core$async$Pub$sub_STAR_$arity$4;
} else {
return and__3618__auto__;
}
})()){
return p.cljs$core$async$Pub$sub_STAR_$arity$4(p,v,ch,close_QMARK_);
} else {
var x__4274__auto__ = (((p == null))?null:p);
return (function (){var or__3630__auto__ = (cljs.core.async.sub_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.sub_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Pub.sub*",p);
}
}
})().call(null,p,v,ch,close_QMARK_);
}
});

cljs.core.async.unsub_STAR_ = (function unsub_STAR_(p,v,ch){
if((function (){var and__3618__auto__ = p;
if(and__3618__auto__){
return p.cljs$core$async$Pub$unsub_STAR_$arity$3;
} else {
return and__3618__auto__;
}
})()){
return p.cljs$core$async$Pub$unsub_STAR_$arity$3(p,v,ch);
} else {
var x__4274__auto__ = (((p == null))?null:p);
return (function (){var or__3630__auto__ = (cljs.core.async.unsub_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.unsub_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Pub.unsub*",p);
}
}
})().call(null,p,v,ch);
}
});

cljs.core.async.unsub_all_STAR_ = (function() {
var unsub_all_STAR_ = null;
var unsub_all_STAR___1 = (function (p){
if((function (){var and__3618__auto__ = p;
if(and__3618__auto__){
return p.cljs$core$async$Pub$unsub_all_STAR_$arity$1;
} else {
return and__3618__auto__;
}
})()){
return p.cljs$core$async$Pub$unsub_all_STAR_$arity$1(p);
} else {
var x__4274__auto__ = (((p == null))?null:p);
return (function (){var or__3630__auto__ = (cljs.core.async.unsub_all_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.unsub_all_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Pub.unsub-all*",p);
}
}
})().call(null,p);
}
});
var unsub_all_STAR___2 = (function (p,v){
if((function (){var and__3618__auto__ = p;
if(and__3618__auto__){
return p.cljs$core$async$Pub$unsub_all_STAR_$arity$2;
} else {
return and__3618__auto__;
}
})()){
return p.cljs$core$async$Pub$unsub_all_STAR_$arity$2(p,v);
} else {
var x__4274__auto__ = (((p == null))?null:p);
return (function (){var or__3630__auto__ = (cljs.core.async.unsub_all_STAR_[goog.typeOf(x__4274__auto__)]);
if(or__3630__auto__){
return or__3630__auto__;
} else {
var or__3630__auto____$1 = (cljs.core.async.unsub_all_STAR_["_"]);
if(or__3630__auto____$1){
return or__3630__auto____$1;
} else {
throw cljs.core.missing_protocol.call(null,"Pub.unsub-all*",p);
}
}
})().call(null,p,v);
}
});
unsub_all_STAR_ = function(p,v){
switch(arguments.length){
case 1:
return unsub_all_STAR___1.call(this,p);
case 2:
return unsub_all_STAR___2.call(this,p,v);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
unsub_all_STAR_.cljs$core$IFn$_invoke$arity$1 = unsub_all_STAR___1;
unsub_all_STAR_.cljs$core$IFn$_invoke$arity$2 = unsub_all_STAR___2;
return unsub_all_STAR_;
})()
;

/**
* Creates and returns a pub(lication) of the supplied channel,
* partitioned into topics by the topic-fn. topic-fn will be applied to
* each value on the channel and the result will determine the 'topic'
* on which that value will be put. Channels can be subscribed to
* receive copies of topics using 'sub', and unsubscribed using
* 'unsub'. Each topic will be handled by an internal mult on a
* dedicated channel. By default these internal channels are
* unbuffered, but a buf-fn can be supplied which, given a topic,
* creates a buffer with desired properties.
* 
* Each item is distributed to all subs in parallel and synchronously,
* i.e. each sub must accept before the next item is distributed. Use
* buffering/windowing to prevent slow subs from holding up the pub.
* 
* Items received when there are no matching subs get dropped.
* 
* Note that if buf-fns are used then each topic is handled
* asynchronously, i.e. if a channel is subscribed to more than one
* topic it should not expect them to be interleaved identically with
* the source.
*/
cljs.core.async.pub = (function() {
var pub = null;
var pub__2 = (function (ch,topic_fn){
return pub.call(null,ch,topic_fn,cljs.core.constantly.call(null,null));
});
var pub__3 = (function (ch,topic_fn,buf_fn){
var mults = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var ensure_mult = ((function (mults){
return (function (topic){
var or__3630__auto__ = cljs.core.get.call(null,cljs.core.deref.call(null,mults),topic);
if(cljs.core.truth_(or__3630__auto__)){
return or__3630__auto__;
} else {
return cljs.core.get.call(null,cljs.core.swap_BANG_.call(null,mults,((function (or__3630__auto__,mults){
return (function (p1__13923_SHARP_){
if(cljs.core.truth_(p1__13923_SHARP_.call(null,topic))){
return p1__13923_SHARP_;
} else {
return cljs.core.assoc.call(null,p1__13923_SHARP_,topic,cljs.core.async.mult.call(null,cljs.core.async.chan.call(null,buf_fn.call(null,topic))));
}
});})(or__3630__auto__,mults))
),topic);
}
});})(mults))
;
var p = (function (){
if(typeof cljs.core.async.t14046 !== 'undefined'){
} else {

/**
* @constructor
*/
cljs.core.async.t14046 = (function (ensure_mult,mults,buf_fn,topic_fn,ch,pub,meta14047){
this.ensure_mult = ensure_mult;
this.mults = mults;
this.buf_fn = buf_fn;
this.topic_fn = topic_fn;
this.ch = ch;
this.pub = pub;
this.meta14047 = meta14047;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
cljs.core.async.t14046.prototype.cljs$core$async$Pub$ = true;

cljs.core.async.t14046.prototype.cljs$core$async$Pub$sub_STAR_$arity$4 = ((function (mults,ensure_mult){
return (function (p,topic,ch__$1,close_QMARK_){
var self__ = this;
var p__$1 = this;
var m = self__.ensure_mult.call(null,topic);
return cljs.core.async.tap.call(null,m,ch__$1,close_QMARK_);
});})(mults,ensure_mult))
;

cljs.core.async.t14046.prototype.cljs$core$async$Pub$unsub_STAR_$arity$3 = ((function (mults,ensure_mult){
return (function (p,topic,ch__$1){
var self__ = this;
var p__$1 = this;
var temp__4126__auto__ = cljs.core.get.call(null,cljs.core.deref.call(null,self__.mults),topic);
if(cljs.core.truth_(temp__4126__auto__)){
var m = temp__4126__auto__;
return cljs.core.async.untap.call(null,m,ch__$1);
} else {
return null;
}
});})(mults,ensure_mult))
;

cljs.core.async.t14046.prototype.cljs$core$async$Pub$unsub_all_STAR_$arity$1 = ((function (mults,ensure_mult){
return (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.reset_BANG_.call(null,self__.mults,cljs.core.PersistentArrayMap.EMPTY);
});})(mults,ensure_mult))
;

cljs.core.async.t14046.prototype.cljs$core$async$Pub$unsub_all_STAR_$arity$2 = ((function (mults,ensure_mult){
return (function (_,topic){
var self__ = this;
var ___$1 = this;
return cljs.core.swap_BANG_.call(null,self__.mults,cljs.core.dissoc,topic);
});})(mults,ensure_mult))
;

cljs.core.async.t14046.prototype.cljs$core$async$Mux$ = true;

cljs.core.async.t14046.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = ((function (mults,ensure_mult){
return (function (_){
var self__ = this;
var ___$1 = this;
return self__.ch;
});})(mults,ensure_mult))
;

cljs.core.async.t14046.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (mults,ensure_mult){
return (function (_14048){
var self__ = this;
var _14048__$1 = this;
return self__.meta14047;
});})(mults,ensure_mult))
;

cljs.core.async.t14046.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (mults,ensure_mult){
return (function (_14048,meta14047__$1){
var self__ = this;
var _14048__$1 = this;
return (new cljs.core.async.t14046(self__.ensure_mult,self__.mults,self__.buf_fn,self__.topic_fn,self__.ch,self__.pub,meta14047__$1));
});})(mults,ensure_mult))
;

cljs.core.async.t14046.cljs$lang$type = true;

cljs.core.async.t14046.cljs$lang$ctorStr = "cljs.core.async/t14046";

cljs.core.async.t14046.cljs$lang$ctorPrWriter = ((function (mults,ensure_mult){
return (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"cljs.core.async/t14046");
});})(mults,ensure_mult))
;

cljs.core.async.__GT_t14046 = ((function (mults,ensure_mult){
return (function __GT_t14046(ensure_mult__$1,mults__$1,buf_fn__$1,topic_fn__$1,ch__$1,pub__$1,meta14047){
return (new cljs.core.async.t14046(ensure_mult__$1,mults__$1,buf_fn__$1,topic_fn__$1,ch__$1,pub__$1,meta14047));
});})(mults,ensure_mult))
;

}

return (new cljs.core.async.t14046(ensure_mult,mults,buf_fn,topic_fn,ch,pub,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"line","line",212345235),591,new cljs.core.Keyword(null,"column","column",2078222095),14,new cljs.core.Keyword(null,"end-line","end-line",1837326455),603,new cljs.core.Keyword(null,"end-column","end-column",1425389514),65], null)));
})()
;
var c__6505__auto___14168 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___14168,mults,ensure_mult,p){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___14168,mults,ensure_mult,p){
return (function (state_14120){
var state_val_14121 = (state_14120[(1)]);
if((state_val_14121 === (7))){
var inst_14116 = (state_14120[(2)]);
var state_14120__$1 = state_14120;
var statearr_14122_14169 = state_14120__$1;
(statearr_14122_14169[(2)] = inst_14116);

(statearr_14122_14169[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (20))){
var state_14120__$1 = state_14120;
var statearr_14123_14170 = state_14120__$1;
(statearr_14123_14170[(2)] = null);

(statearr_14123_14170[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (1))){
var state_14120__$1 = state_14120;
var statearr_14124_14171 = state_14120__$1;
(statearr_14124_14171[(2)] = null);

(statearr_14124_14171[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (24))){
var inst_14099 = (state_14120[(7)]);
var inst_14108 = cljs.core.swap_BANG_.call(null,mults,cljs.core.dissoc,inst_14099);
var state_14120__$1 = state_14120;
var statearr_14125_14172 = state_14120__$1;
(statearr_14125_14172[(2)] = inst_14108);

(statearr_14125_14172[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (4))){
var inst_14051 = (state_14120[(8)]);
var inst_14051__$1 = (state_14120[(2)]);
var inst_14052 = (inst_14051__$1 == null);
var state_14120__$1 = (function (){var statearr_14126 = state_14120;
(statearr_14126[(8)] = inst_14051__$1);

return statearr_14126;
})();
if(cljs.core.truth_(inst_14052)){
var statearr_14127_14173 = state_14120__$1;
(statearr_14127_14173[(1)] = (5));

} else {
var statearr_14128_14174 = state_14120__$1;
(statearr_14128_14174[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (15))){
var inst_14093 = (state_14120[(2)]);
var state_14120__$1 = state_14120;
var statearr_14129_14175 = state_14120__$1;
(statearr_14129_14175[(2)] = inst_14093);

(statearr_14129_14175[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (21))){
var inst_14113 = (state_14120[(2)]);
var state_14120__$1 = (function (){var statearr_14130 = state_14120;
(statearr_14130[(9)] = inst_14113);

return statearr_14130;
})();
var statearr_14131_14176 = state_14120__$1;
(statearr_14131_14176[(2)] = null);

(statearr_14131_14176[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (13))){
var inst_14075 = (state_14120[(10)]);
var inst_14077 = cljs.core.chunked_seq_QMARK_.call(null,inst_14075);
var state_14120__$1 = state_14120;
if(inst_14077){
var statearr_14132_14177 = state_14120__$1;
(statearr_14132_14177[(1)] = (16));

} else {
var statearr_14133_14178 = state_14120__$1;
(statearr_14133_14178[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (22))){
var inst_14105 = (state_14120[(2)]);
var state_14120__$1 = state_14120;
if(cljs.core.truth_(inst_14105)){
var statearr_14134_14179 = state_14120__$1;
(statearr_14134_14179[(1)] = (23));

} else {
var statearr_14135_14180 = state_14120__$1;
(statearr_14135_14180[(1)] = (24));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (6))){
var inst_14101 = (state_14120[(11)]);
var inst_14099 = (state_14120[(7)]);
var inst_14051 = (state_14120[(8)]);
var inst_14099__$1 = topic_fn.call(null,inst_14051);
var inst_14100 = cljs.core.deref.call(null,mults);
var inst_14101__$1 = cljs.core.get.call(null,inst_14100,inst_14099__$1);
var state_14120__$1 = (function (){var statearr_14136 = state_14120;
(statearr_14136[(11)] = inst_14101__$1);

(statearr_14136[(7)] = inst_14099__$1);

return statearr_14136;
})();
if(cljs.core.truth_(inst_14101__$1)){
var statearr_14137_14181 = state_14120__$1;
(statearr_14137_14181[(1)] = (19));

} else {
var statearr_14138_14182 = state_14120__$1;
(statearr_14138_14182[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (25))){
var inst_14110 = (state_14120[(2)]);
var state_14120__$1 = state_14120;
var statearr_14139_14183 = state_14120__$1;
(statearr_14139_14183[(2)] = inst_14110);

(statearr_14139_14183[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (17))){
var inst_14075 = (state_14120[(10)]);
var inst_14084 = cljs.core.first.call(null,inst_14075);
var inst_14085 = cljs.core.async.muxch_STAR_.call(null,inst_14084);
var inst_14086 = cljs.core.async.close_BANG_.call(null,inst_14085);
var inst_14087 = cljs.core.next.call(null,inst_14075);
var inst_14061 = inst_14087;
var inst_14062 = null;
var inst_14063 = (0);
var inst_14064 = (0);
var state_14120__$1 = (function (){var statearr_14140 = state_14120;
(statearr_14140[(12)] = inst_14086);

(statearr_14140[(13)] = inst_14062);

(statearr_14140[(14)] = inst_14063);

(statearr_14140[(15)] = inst_14061);

(statearr_14140[(16)] = inst_14064);

return statearr_14140;
})();
var statearr_14141_14184 = state_14120__$1;
(statearr_14141_14184[(2)] = null);

(statearr_14141_14184[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (3))){
var inst_14118 = (state_14120[(2)]);
var state_14120__$1 = state_14120;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_14120__$1,inst_14118);
} else {
if((state_val_14121 === (12))){
var inst_14095 = (state_14120[(2)]);
var state_14120__$1 = state_14120;
var statearr_14142_14185 = state_14120__$1;
(statearr_14142_14185[(2)] = inst_14095);

(statearr_14142_14185[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (2))){
var state_14120__$1 = state_14120;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_14120__$1,(4),ch);
} else {
if((state_val_14121 === (23))){
var state_14120__$1 = state_14120;
var statearr_14143_14186 = state_14120__$1;
(statearr_14143_14186[(2)] = null);

(statearr_14143_14186[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (19))){
var inst_14101 = (state_14120[(11)]);
var inst_14051 = (state_14120[(8)]);
var inst_14103 = cljs.core.async.muxch_STAR_.call(null,inst_14101);
var state_14120__$1 = state_14120;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_14120__$1,(22),inst_14103,inst_14051);
} else {
if((state_val_14121 === (11))){
var inst_14075 = (state_14120[(10)]);
var inst_14061 = (state_14120[(15)]);
var inst_14075__$1 = cljs.core.seq.call(null,inst_14061);
var state_14120__$1 = (function (){var statearr_14144 = state_14120;
(statearr_14144[(10)] = inst_14075__$1);

return statearr_14144;
})();
if(inst_14075__$1){
var statearr_14145_14187 = state_14120__$1;
(statearr_14145_14187[(1)] = (13));

} else {
var statearr_14146_14188 = state_14120__$1;
(statearr_14146_14188[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (9))){
var inst_14097 = (state_14120[(2)]);
var state_14120__$1 = state_14120;
var statearr_14147_14189 = state_14120__$1;
(statearr_14147_14189[(2)] = inst_14097);

(statearr_14147_14189[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (5))){
var inst_14058 = cljs.core.deref.call(null,mults);
var inst_14059 = cljs.core.vals.call(null,inst_14058);
var inst_14060 = cljs.core.seq.call(null,inst_14059);
var inst_14061 = inst_14060;
var inst_14062 = null;
var inst_14063 = (0);
var inst_14064 = (0);
var state_14120__$1 = (function (){var statearr_14148 = state_14120;
(statearr_14148[(13)] = inst_14062);

(statearr_14148[(14)] = inst_14063);

(statearr_14148[(15)] = inst_14061);

(statearr_14148[(16)] = inst_14064);

return statearr_14148;
})();
var statearr_14149_14190 = state_14120__$1;
(statearr_14149_14190[(2)] = null);

(statearr_14149_14190[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (14))){
var state_14120__$1 = state_14120;
var statearr_14153_14191 = state_14120__$1;
(statearr_14153_14191[(2)] = null);

(statearr_14153_14191[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (16))){
var inst_14075 = (state_14120[(10)]);
var inst_14079 = cljs.core.chunk_first.call(null,inst_14075);
var inst_14080 = cljs.core.chunk_rest.call(null,inst_14075);
var inst_14081 = cljs.core.count.call(null,inst_14079);
var inst_14061 = inst_14080;
var inst_14062 = inst_14079;
var inst_14063 = inst_14081;
var inst_14064 = (0);
var state_14120__$1 = (function (){var statearr_14154 = state_14120;
(statearr_14154[(13)] = inst_14062);

(statearr_14154[(14)] = inst_14063);

(statearr_14154[(15)] = inst_14061);

(statearr_14154[(16)] = inst_14064);

return statearr_14154;
})();
var statearr_14155_14192 = state_14120__$1;
(statearr_14155_14192[(2)] = null);

(statearr_14155_14192[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (10))){
var inst_14062 = (state_14120[(13)]);
var inst_14063 = (state_14120[(14)]);
var inst_14061 = (state_14120[(15)]);
var inst_14064 = (state_14120[(16)]);
var inst_14069 = cljs.core._nth.call(null,inst_14062,inst_14064);
var inst_14070 = cljs.core.async.muxch_STAR_.call(null,inst_14069);
var inst_14071 = cljs.core.async.close_BANG_.call(null,inst_14070);
var inst_14072 = (inst_14064 + (1));
var tmp14150 = inst_14062;
var tmp14151 = inst_14063;
var tmp14152 = inst_14061;
var inst_14061__$1 = tmp14152;
var inst_14062__$1 = tmp14150;
var inst_14063__$1 = tmp14151;
var inst_14064__$1 = inst_14072;
var state_14120__$1 = (function (){var statearr_14156 = state_14120;
(statearr_14156[(13)] = inst_14062__$1);

(statearr_14156[(14)] = inst_14063__$1);

(statearr_14156[(15)] = inst_14061__$1);

(statearr_14156[(17)] = inst_14071);

(statearr_14156[(16)] = inst_14064__$1);

return statearr_14156;
})();
var statearr_14157_14193 = state_14120__$1;
(statearr_14157_14193[(2)] = null);

(statearr_14157_14193[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (18))){
var inst_14090 = (state_14120[(2)]);
var state_14120__$1 = state_14120;
var statearr_14158_14194 = state_14120__$1;
(statearr_14158_14194[(2)] = inst_14090);

(statearr_14158_14194[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14121 === (8))){
var inst_14063 = (state_14120[(14)]);
var inst_14064 = (state_14120[(16)]);
var inst_14066 = (inst_14064 < inst_14063);
var inst_14067 = inst_14066;
var state_14120__$1 = state_14120;
if(cljs.core.truth_(inst_14067)){
var statearr_14159_14195 = state_14120__$1;
(statearr_14159_14195[(1)] = (10));

} else {
var statearr_14160_14196 = state_14120__$1;
(statearr_14160_14196[(1)] = (11));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___14168,mults,ensure_mult,p))
;
return ((function (switch__6490__auto__,c__6505__auto___14168,mults,ensure_mult,p){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_14164 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_14164[(0)] = state_machine__6491__auto__);

(statearr_14164[(1)] = (1));

return statearr_14164;
});
var state_machine__6491__auto____1 = (function (state_14120){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_14120);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e14165){if((e14165 instanceof Object)){
var ex__6494__auto__ = e14165;
var statearr_14166_14197 = state_14120;
(statearr_14166_14197[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_14120);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e14165;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__14198 = state_14120;
state_14120 = G__14198;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_14120){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_14120);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___14168,mults,ensure_mult,p))
})();
var state__6507__auto__ = (function (){var statearr_14167 = f__6506__auto__.call(null);
(statearr_14167[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___14168);

return statearr_14167;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___14168,mults,ensure_mult,p))
);


return p;
});
pub = function(ch,topic_fn,buf_fn){
switch(arguments.length){
case 2:
return pub__2.call(this,ch,topic_fn);
case 3:
return pub__3.call(this,ch,topic_fn,buf_fn);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
pub.cljs$core$IFn$_invoke$arity$2 = pub__2;
pub.cljs$core$IFn$_invoke$arity$3 = pub__3;
return pub;
})()
;
/**
* Subscribes a channel to a topic of a pub.
* 
* By default the channel will be closed when the source closes,
* but can be determined by the close? parameter.
*/
cljs.core.async.sub = (function() {
var sub = null;
var sub__3 = (function (p,topic,ch){
return sub.call(null,p,topic,ch,true);
});
var sub__4 = (function (p,topic,ch,close_QMARK_){
return cljs.core.async.sub_STAR_.call(null,p,topic,ch,close_QMARK_);
});
sub = function(p,topic,ch,close_QMARK_){
switch(arguments.length){
case 3:
return sub__3.call(this,p,topic,ch);
case 4:
return sub__4.call(this,p,topic,ch,close_QMARK_);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
sub.cljs$core$IFn$_invoke$arity$3 = sub__3;
sub.cljs$core$IFn$_invoke$arity$4 = sub__4;
return sub;
})()
;
/**
* Unsubscribes a channel from a topic of a pub
*/
cljs.core.async.unsub = (function unsub(p,topic,ch){
return cljs.core.async.unsub_STAR_.call(null,p,topic,ch);
});
/**
* Unsubscribes all channels from a pub, or a topic of a pub
*/
cljs.core.async.unsub_all = (function() {
var unsub_all = null;
var unsub_all__1 = (function (p){
return cljs.core.async.unsub_all_STAR_.call(null,p);
});
var unsub_all__2 = (function (p,topic){
return cljs.core.async.unsub_all_STAR_.call(null,p,topic);
});
unsub_all = function(p,topic){
switch(arguments.length){
case 1:
return unsub_all__1.call(this,p);
case 2:
return unsub_all__2.call(this,p,topic);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
unsub_all.cljs$core$IFn$_invoke$arity$1 = unsub_all__1;
unsub_all.cljs$core$IFn$_invoke$arity$2 = unsub_all__2;
return unsub_all;
})()
;
/**
* Takes a function and a collection of source channels, and returns a
* channel which contains the values produced by applying f to the set
* of first items taken from each source channel, followed by applying
* f to the set of second items from each channel, until any one of the
* channels is closed, at which point the output channel will be
* closed. The returned channel will be unbuffered by default, or a
* buf-or-n can be supplied
*/
cljs.core.async.map = (function() {
var map = null;
var map__2 = (function (f,chs){
return map.call(null,f,chs,null);
});
var map__3 = (function (f,chs,buf_or_n){
var chs__$1 = cljs.core.vec.call(null,chs);
var out = cljs.core.async.chan.call(null,buf_or_n);
var cnt = cljs.core.count.call(null,chs__$1);
var rets = cljs.core.object_array.call(null,cnt);
var dchan = cljs.core.async.chan.call(null,(1));
var dctr = cljs.core.atom.call(null,null);
var done = cljs.core.mapv.call(null,((function (chs__$1,out,cnt,rets,dchan,dctr){
return (function (i){
return ((function (chs__$1,out,cnt,rets,dchan,dctr){
return (function (ret){
(rets[i] = ret);

if((cljs.core.swap_BANG_.call(null,dctr,cljs.core.dec) === (0))){
return cljs.core.async.put_BANG_.call(null,dchan,rets.slice((0)));
} else {
return null;
}
});
;})(chs__$1,out,cnt,rets,dchan,dctr))
});})(chs__$1,out,cnt,rets,dchan,dctr))
,cljs.core.range.call(null,cnt));
var c__6505__auto___14335 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___14335,chs__$1,out,cnt,rets,dchan,dctr,done){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___14335,chs__$1,out,cnt,rets,dchan,dctr,done){
return (function (state_14305){
var state_val_14306 = (state_14305[(1)]);
if((state_val_14306 === (7))){
var state_14305__$1 = state_14305;
var statearr_14307_14336 = state_14305__$1;
(statearr_14307_14336[(2)] = null);

(statearr_14307_14336[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14306 === (1))){
var state_14305__$1 = state_14305;
var statearr_14308_14337 = state_14305__$1;
(statearr_14308_14337[(2)] = null);

(statearr_14308_14337[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14306 === (4))){
var inst_14269 = (state_14305[(7)]);
var inst_14271 = (inst_14269 < cnt);
var state_14305__$1 = state_14305;
if(cljs.core.truth_(inst_14271)){
var statearr_14309_14338 = state_14305__$1;
(statearr_14309_14338[(1)] = (6));

} else {
var statearr_14310_14339 = state_14305__$1;
(statearr_14310_14339[(1)] = (7));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14306 === (15))){
var inst_14301 = (state_14305[(2)]);
var state_14305__$1 = state_14305;
var statearr_14311_14340 = state_14305__$1;
(statearr_14311_14340[(2)] = inst_14301);

(statearr_14311_14340[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14306 === (13))){
var inst_14294 = cljs.core.async.close_BANG_.call(null,out);
var state_14305__$1 = state_14305;
var statearr_14312_14341 = state_14305__$1;
(statearr_14312_14341[(2)] = inst_14294);

(statearr_14312_14341[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14306 === (6))){
var state_14305__$1 = state_14305;
var statearr_14313_14342 = state_14305__$1;
(statearr_14313_14342[(2)] = null);

(statearr_14313_14342[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14306 === (3))){
var inst_14303 = (state_14305[(2)]);
var state_14305__$1 = state_14305;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_14305__$1,inst_14303);
} else {
if((state_val_14306 === (12))){
var inst_14291 = (state_14305[(8)]);
var inst_14291__$1 = (state_14305[(2)]);
var inst_14292 = cljs.core.some.call(null,cljs.core.nil_QMARK_,inst_14291__$1);
var state_14305__$1 = (function (){var statearr_14314 = state_14305;
(statearr_14314[(8)] = inst_14291__$1);

return statearr_14314;
})();
if(cljs.core.truth_(inst_14292)){
var statearr_14315_14343 = state_14305__$1;
(statearr_14315_14343[(1)] = (13));

} else {
var statearr_14316_14344 = state_14305__$1;
(statearr_14316_14344[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14306 === (2))){
var inst_14268 = cljs.core.reset_BANG_.call(null,dctr,cnt);
var inst_14269 = (0);
var state_14305__$1 = (function (){var statearr_14317 = state_14305;
(statearr_14317[(9)] = inst_14268);

(statearr_14317[(7)] = inst_14269);

return statearr_14317;
})();
var statearr_14318_14345 = state_14305__$1;
(statearr_14318_14345[(2)] = null);

(statearr_14318_14345[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14306 === (11))){
var inst_14269 = (state_14305[(7)]);
var _ = cljs.core.async.impl.ioc_helpers.add_exception_frame.call(null,state_14305,(10),Object,null,(9));
var inst_14278 = chs__$1.call(null,inst_14269);
var inst_14279 = done.call(null,inst_14269);
var inst_14280 = cljs.core.async.take_BANG_.call(null,inst_14278,inst_14279);
var state_14305__$1 = state_14305;
var statearr_14319_14346 = state_14305__$1;
(statearr_14319_14346[(2)] = inst_14280);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_14305__$1);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14306 === (9))){
var inst_14269 = (state_14305[(7)]);
var inst_14282 = (state_14305[(2)]);
var inst_14283 = (inst_14269 + (1));
var inst_14269__$1 = inst_14283;
var state_14305__$1 = (function (){var statearr_14320 = state_14305;
(statearr_14320[(10)] = inst_14282);

(statearr_14320[(7)] = inst_14269__$1);

return statearr_14320;
})();
var statearr_14321_14347 = state_14305__$1;
(statearr_14321_14347[(2)] = null);

(statearr_14321_14347[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14306 === (5))){
var inst_14289 = (state_14305[(2)]);
var state_14305__$1 = (function (){var statearr_14322 = state_14305;
(statearr_14322[(11)] = inst_14289);

return statearr_14322;
})();
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_14305__$1,(12),dchan);
} else {
if((state_val_14306 === (14))){
var inst_14291 = (state_14305[(8)]);
var inst_14296 = cljs.core.apply.call(null,f,inst_14291);
var state_14305__$1 = state_14305;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_14305__$1,(16),out,inst_14296);
} else {
if((state_val_14306 === (16))){
var inst_14298 = (state_14305[(2)]);
var state_14305__$1 = (function (){var statearr_14323 = state_14305;
(statearr_14323[(12)] = inst_14298);

return statearr_14323;
})();
var statearr_14324_14348 = state_14305__$1;
(statearr_14324_14348[(2)] = null);

(statearr_14324_14348[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14306 === (10))){
var inst_14273 = (state_14305[(2)]);
var inst_14274 = cljs.core.swap_BANG_.call(null,dctr,cljs.core.dec);
var state_14305__$1 = (function (){var statearr_14325 = state_14305;
(statearr_14325[(13)] = inst_14273);

return statearr_14325;
})();
var statearr_14326_14349 = state_14305__$1;
(statearr_14326_14349[(2)] = inst_14274);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_14305__$1);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14306 === (8))){
var inst_14287 = (state_14305[(2)]);
var state_14305__$1 = state_14305;
var statearr_14327_14350 = state_14305__$1;
(statearr_14327_14350[(2)] = inst_14287);

(statearr_14327_14350[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___14335,chs__$1,out,cnt,rets,dchan,dctr,done))
;
return ((function (switch__6490__auto__,c__6505__auto___14335,chs__$1,out,cnt,rets,dchan,dctr,done){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_14331 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_14331[(0)] = state_machine__6491__auto__);

(statearr_14331[(1)] = (1));

return statearr_14331;
});
var state_machine__6491__auto____1 = (function (state_14305){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_14305);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e14332){if((e14332 instanceof Object)){
var ex__6494__auto__ = e14332;
var statearr_14333_14351 = state_14305;
(statearr_14333_14351[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_14305);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e14332;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__14352 = state_14305;
state_14305 = G__14352;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_14305){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_14305);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___14335,chs__$1,out,cnt,rets,dchan,dctr,done))
})();
var state__6507__auto__ = (function (){var statearr_14334 = f__6506__auto__.call(null);
(statearr_14334[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___14335);

return statearr_14334;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___14335,chs__$1,out,cnt,rets,dchan,dctr,done))
);


return out;
});
map = function(f,chs,buf_or_n){
switch(arguments.length){
case 2:
return map__2.call(this,f,chs);
case 3:
return map__3.call(this,f,chs,buf_or_n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
map.cljs$core$IFn$_invoke$arity$2 = map__2;
map.cljs$core$IFn$_invoke$arity$3 = map__3;
return map;
})()
;
/**
* Takes a collection of source channels and returns a channel which
* contains all values taken from them. The returned channel will be
* unbuffered by default, or a buf-or-n can be supplied. The channel
* will close after all the source channels have closed.
*/
cljs.core.async.merge = (function() {
var merge = null;
var merge__1 = (function (chs){
return merge.call(null,chs,null);
});
var merge__2 = (function (chs,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
var c__6505__auto___14460 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___14460,out){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___14460,out){
return (function (state_14436){
var state_val_14437 = (state_14436[(1)]);
if((state_val_14437 === (7))){
var inst_14416 = (state_14436[(7)]);
var inst_14415 = (state_14436[(8)]);
var inst_14415__$1 = (state_14436[(2)]);
var inst_14416__$1 = cljs.core.nth.call(null,inst_14415__$1,(0),null);
var inst_14417 = cljs.core.nth.call(null,inst_14415__$1,(1),null);
var inst_14418 = (inst_14416__$1 == null);
var state_14436__$1 = (function (){var statearr_14438 = state_14436;
(statearr_14438[(7)] = inst_14416__$1);

(statearr_14438[(8)] = inst_14415__$1);

(statearr_14438[(9)] = inst_14417);

return statearr_14438;
})();
if(cljs.core.truth_(inst_14418)){
var statearr_14439_14461 = state_14436__$1;
(statearr_14439_14461[(1)] = (8));

} else {
var statearr_14440_14462 = state_14436__$1;
(statearr_14440_14462[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14437 === (1))){
var inst_14407 = cljs.core.vec.call(null,chs);
var inst_14408 = inst_14407;
var state_14436__$1 = (function (){var statearr_14441 = state_14436;
(statearr_14441[(10)] = inst_14408);

return statearr_14441;
})();
var statearr_14442_14463 = state_14436__$1;
(statearr_14442_14463[(2)] = null);

(statearr_14442_14463[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14437 === (4))){
var inst_14408 = (state_14436[(10)]);
var state_14436__$1 = state_14436;
return cljs.core.async.ioc_alts_BANG_.call(null,state_14436__$1,(7),inst_14408);
} else {
if((state_val_14437 === (6))){
var inst_14432 = (state_14436[(2)]);
var state_14436__$1 = state_14436;
var statearr_14443_14464 = state_14436__$1;
(statearr_14443_14464[(2)] = inst_14432);

(statearr_14443_14464[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14437 === (3))){
var inst_14434 = (state_14436[(2)]);
var state_14436__$1 = state_14436;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_14436__$1,inst_14434);
} else {
if((state_val_14437 === (2))){
var inst_14408 = (state_14436[(10)]);
var inst_14410 = cljs.core.count.call(null,inst_14408);
var inst_14411 = (inst_14410 > (0));
var state_14436__$1 = state_14436;
if(cljs.core.truth_(inst_14411)){
var statearr_14445_14465 = state_14436__$1;
(statearr_14445_14465[(1)] = (4));

} else {
var statearr_14446_14466 = state_14436__$1;
(statearr_14446_14466[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14437 === (11))){
var inst_14408 = (state_14436[(10)]);
var inst_14425 = (state_14436[(2)]);
var tmp14444 = inst_14408;
var inst_14408__$1 = tmp14444;
var state_14436__$1 = (function (){var statearr_14447 = state_14436;
(statearr_14447[(10)] = inst_14408__$1);

(statearr_14447[(11)] = inst_14425);

return statearr_14447;
})();
var statearr_14448_14467 = state_14436__$1;
(statearr_14448_14467[(2)] = null);

(statearr_14448_14467[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14437 === (9))){
var inst_14416 = (state_14436[(7)]);
var state_14436__$1 = state_14436;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_14436__$1,(11),out,inst_14416);
} else {
if((state_val_14437 === (5))){
var inst_14430 = cljs.core.async.close_BANG_.call(null,out);
var state_14436__$1 = state_14436;
var statearr_14449_14468 = state_14436__$1;
(statearr_14449_14468[(2)] = inst_14430);

(statearr_14449_14468[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14437 === (10))){
var inst_14428 = (state_14436[(2)]);
var state_14436__$1 = state_14436;
var statearr_14450_14469 = state_14436__$1;
(statearr_14450_14469[(2)] = inst_14428);

(statearr_14450_14469[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14437 === (8))){
var inst_14408 = (state_14436[(10)]);
var inst_14416 = (state_14436[(7)]);
var inst_14415 = (state_14436[(8)]);
var inst_14417 = (state_14436[(9)]);
var inst_14420 = (function (){var c = inst_14417;
var v = inst_14416;
var vec__14413 = inst_14415;
var cs = inst_14408;
return ((function (c,v,vec__14413,cs,inst_14408,inst_14416,inst_14415,inst_14417,state_val_14437,c__6505__auto___14460,out){
return (function (p1__14353_SHARP_){
return cljs.core.not_EQ_.call(null,c,p1__14353_SHARP_);
});
;})(c,v,vec__14413,cs,inst_14408,inst_14416,inst_14415,inst_14417,state_val_14437,c__6505__auto___14460,out))
})();
var inst_14421 = cljs.core.filterv.call(null,inst_14420,inst_14408);
var inst_14408__$1 = inst_14421;
var state_14436__$1 = (function (){var statearr_14451 = state_14436;
(statearr_14451[(10)] = inst_14408__$1);

return statearr_14451;
})();
var statearr_14452_14470 = state_14436__$1;
(statearr_14452_14470[(2)] = null);

(statearr_14452_14470[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___14460,out))
;
return ((function (switch__6490__auto__,c__6505__auto___14460,out){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_14456 = [null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_14456[(0)] = state_machine__6491__auto__);

(statearr_14456[(1)] = (1));

return statearr_14456;
});
var state_machine__6491__auto____1 = (function (state_14436){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_14436);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e14457){if((e14457 instanceof Object)){
var ex__6494__auto__ = e14457;
var statearr_14458_14471 = state_14436;
(statearr_14458_14471[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_14436);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e14457;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__14472 = state_14436;
state_14436 = G__14472;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_14436){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_14436);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___14460,out))
})();
var state__6507__auto__ = (function (){var statearr_14459 = f__6506__auto__.call(null);
(statearr_14459[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___14460);

return statearr_14459;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___14460,out))
);


return out;
});
merge = function(chs,buf_or_n){
switch(arguments.length){
case 1:
return merge__1.call(this,chs);
case 2:
return merge__2.call(this,chs,buf_or_n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
merge.cljs$core$IFn$_invoke$arity$1 = merge__1;
merge.cljs$core$IFn$_invoke$arity$2 = merge__2;
return merge;
})()
;
/**
* Returns a channel containing the single (collection) result of the
* items taken from the channel conjoined to the supplied
* collection. ch must close before into produces a result.
*/
cljs.core.async.into = (function into(coll,ch){
return cljs.core.async.reduce.call(null,cljs.core.conj,coll,ch);
});
/**
* Returns a channel that will return, at most, n items from ch. After n items
* have been returned, or ch has been closed, the return chanel will close.
* 
* The output channel is unbuffered by default, unless buf-or-n is given.
*/
cljs.core.async.take = (function() {
var take = null;
var take__2 = (function (n,ch){
return take.call(null,n,ch,null);
});
var take__3 = (function (n,ch,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
var c__6505__auto___14565 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___14565,out){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___14565,out){
return (function (state_14542){
var state_val_14543 = (state_14542[(1)]);
if((state_val_14543 === (7))){
var inst_14524 = (state_14542[(7)]);
var inst_14524__$1 = (state_14542[(2)]);
var inst_14525 = (inst_14524__$1 == null);
var inst_14526 = cljs.core.not.call(null,inst_14525);
var state_14542__$1 = (function (){var statearr_14544 = state_14542;
(statearr_14544[(7)] = inst_14524__$1);

return statearr_14544;
})();
if(inst_14526){
var statearr_14545_14566 = state_14542__$1;
(statearr_14545_14566[(1)] = (8));

} else {
var statearr_14546_14567 = state_14542__$1;
(statearr_14546_14567[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14543 === (1))){
var inst_14519 = (0);
var state_14542__$1 = (function (){var statearr_14547 = state_14542;
(statearr_14547[(8)] = inst_14519);

return statearr_14547;
})();
var statearr_14548_14568 = state_14542__$1;
(statearr_14548_14568[(2)] = null);

(statearr_14548_14568[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14543 === (4))){
var state_14542__$1 = state_14542;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_14542__$1,(7),ch);
} else {
if((state_val_14543 === (6))){
var inst_14537 = (state_14542[(2)]);
var state_14542__$1 = state_14542;
var statearr_14549_14569 = state_14542__$1;
(statearr_14549_14569[(2)] = inst_14537);

(statearr_14549_14569[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14543 === (3))){
var inst_14539 = (state_14542[(2)]);
var inst_14540 = cljs.core.async.close_BANG_.call(null,out);
var state_14542__$1 = (function (){var statearr_14550 = state_14542;
(statearr_14550[(9)] = inst_14539);

return statearr_14550;
})();
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_14542__$1,inst_14540);
} else {
if((state_val_14543 === (2))){
var inst_14519 = (state_14542[(8)]);
var inst_14521 = (inst_14519 < n);
var state_14542__$1 = state_14542;
if(cljs.core.truth_(inst_14521)){
var statearr_14551_14570 = state_14542__$1;
(statearr_14551_14570[(1)] = (4));

} else {
var statearr_14552_14571 = state_14542__$1;
(statearr_14552_14571[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14543 === (11))){
var inst_14519 = (state_14542[(8)]);
var inst_14529 = (state_14542[(2)]);
var inst_14530 = (inst_14519 + (1));
var inst_14519__$1 = inst_14530;
var state_14542__$1 = (function (){var statearr_14553 = state_14542;
(statearr_14553[(10)] = inst_14529);

(statearr_14553[(8)] = inst_14519__$1);

return statearr_14553;
})();
var statearr_14554_14572 = state_14542__$1;
(statearr_14554_14572[(2)] = null);

(statearr_14554_14572[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14543 === (9))){
var state_14542__$1 = state_14542;
var statearr_14555_14573 = state_14542__$1;
(statearr_14555_14573[(2)] = null);

(statearr_14555_14573[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14543 === (5))){
var state_14542__$1 = state_14542;
var statearr_14556_14574 = state_14542__$1;
(statearr_14556_14574[(2)] = null);

(statearr_14556_14574[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14543 === (10))){
var inst_14534 = (state_14542[(2)]);
var state_14542__$1 = state_14542;
var statearr_14557_14575 = state_14542__$1;
(statearr_14557_14575[(2)] = inst_14534);

(statearr_14557_14575[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14543 === (8))){
var inst_14524 = (state_14542[(7)]);
var state_14542__$1 = state_14542;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_14542__$1,(11),out,inst_14524);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___14565,out))
;
return ((function (switch__6490__auto__,c__6505__auto___14565,out){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_14561 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_14561[(0)] = state_machine__6491__auto__);

(statearr_14561[(1)] = (1));

return statearr_14561;
});
var state_machine__6491__auto____1 = (function (state_14542){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_14542);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e14562){if((e14562 instanceof Object)){
var ex__6494__auto__ = e14562;
var statearr_14563_14576 = state_14542;
(statearr_14563_14576[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_14542);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e14562;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__14577 = state_14542;
state_14542 = G__14577;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_14542){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_14542);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___14565,out))
})();
var state__6507__auto__ = (function (){var statearr_14564 = f__6506__auto__.call(null);
(statearr_14564[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___14565);

return statearr_14564;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___14565,out))
);


return out;
});
take = function(n,ch,buf_or_n){
switch(arguments.length){
case 2:
return take__2.call(this,n,ch);
case 3:
return take__3.call(this,n,ch,buf_or_n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
take.cljs$core$IFn$_invoke$arity$2 = take__2;
take.cljs$core$IFn$_invoke$arity$3 = take__3;
return take;
})()
;
/**
* Deprecated - this function will be removed. Use transducer instead
*/
cljs.core.async.map_LT_ = (function map_LT_(f,ch){
if(typeof cljs.core.async.t14585 !== 'undefined'){
} else {

/**
* @constructor
*/
cljs.core.async.t14585 = (function (ch,f,map_LT_,meta14586){
this.ch = ch;
this.f = f;
this.map_LT_ = map_LT_;
this.meta14586 = meta14586;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
cljs.core.async.t14585.prototype.cljs$core$async$impl$protocols$WritePort$ = true;

cljs.core.async.t14585.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.put_BANG_.call(null,self__.ch,val,fn1);
});

cljs.core.async.t14585.prototype.cljs$core$async$impl$protocols$ReadPort$ = true;

cljs.core.async.t14585.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
var ret = cljs.core.async.impl.protocols.take_BANG_.call(null,self__.ch,(function (){
if(typeof cljs.core.async.t14588 !== 'undefined'){
} else {

/**
* @constructor
*/
cljs.core.async.t14588 = (function (fn1,_,meta14586,map_LT_,f,ch,meta14589){
this.fn1 = fn1;
this._ = _;
this.meta14586 = meta14586;
this.map_LT_ = map_LT_;
this.f = f;
this.ch = ch;
this.meta14589 = meta14589;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
cljs.core.async.t14588.prototype.cljs$core$async$impl$protocols$Handler$ = true;

cljs.core.async.t14588.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = ((function (___$1){
return (function (___$1){
var self__ = this;
var ___$2 = this;
return cljs.core.async.impl.protocols.active_QMARK_.call(null,self__.fn1);
});})(___$1))
;

cljs.core.async.t14588.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = ((function (___$1){
return (function (___$1){
var self__ = this;
var ___$2 = this;
var f1 = cljs.core.async.impl.protocols.commit.call(null,self__.fn1);
return ((function (f1,___$2,___$1){
return (function (p1__14578_SHARP_){
return f1.call(null,(((p1__14578_SHARP_ == null))?null:self__.f.call(null,p1__14578_SHARP_)));
});
;})(f1,___$2,___$1))
});})(___$1))
;

cljs.core.async.t14588.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (___$1){
return (function (_14590){
var self__ = this;
var _14590__$1 = this;
return self__.meta14589;
});})(___$1))
;

cljs.core.async.t14588.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (___$1){
return (function (_14590,meta14589__$1){
var self__ = this;
var _14590__$1 = this;
return (new cljs.core.async.t14588(self__.fn1,self__._,self__.meta14586,self__.map_LT_,self__.f,self__.ch,meta14589__$1));
});})(___$1))
;

cljs.core.async.t14588.cljs$lang$type = true;

cljs.core.async.t14588.cljs$lang$ctorStr = "cljs.core.async/t14588";

cljs.core.async.t14588.cljs$lang$ctorPrWriter = ((function (___$1){
return (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"cljs.core.async/t14588");
});})(___$1))
;

cljs.core.async.__GT_t14588 = ((function (___$1){
return (function __GT_t14588(fn1__$1,___$2,meta14586__$1,map_LT___$1,f__$1,ch__$1,meta14589){
return (new cljs.core.async.t14588(fn1__$1,___$2,meta14586__$1,map_LT___$1,f__$1,ch__$1,meta14589));
});})(___$1))
;

}

return (new cljs.core.async.t14588(fn1,___$1,self__.meta14586,self__.map_LT_,self__.f,self__.ch,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"line","line",212345235),731,new cljs.core.Keyword(null,"column","column",2078222095),10,new cljs.core.Keyword(null,"end-line","end-line",1837326455),737,new cljs.core.Keyword(null,"end-column","end-column",1425389514),46], null)));
})()
);
if(cljs.core.truth_((function (){var and__3618__auto__ = ret;
if(cljs.core.truth_(and__3618__auto__)){
return !((cljs.core.deref.call(null,ret) == null));
} else {
return and__3618__auto__;
}
})())){
return cljs.core.async.impl.channels.box.call(null,self__.f.call(null,cljs.core.deref.call(null,ret)));
} else {
return ret;
}
});

cljs.core.async.t14585.prototype.cljs$core$async$impl$protocols$Channel$ = true;

cljs.core.async.t14585.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_.call(null,self__.ch);
});

cljs.core.async.t14585.prototype.cljs$core$async$impl$protocols$Channel$closed_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.closed_QMARK_.call(null,self__.ch);
});

cljs.core.async.t14585.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_14587){
var self__ = this;
var _14587__$1 = this;
return self__.meta14586;
});

cljs.core.async.t14585.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_14587,meta14586__$1){
var self__ = this;
var _14587__$1 = this;
return (new cljs.core.async.t14585(self__.ch,self__.f,self__.map_LT_,meta14586__$1));
});

cljs.core.async.t14585.cljs$lang$type = true;

cljs.core.async.t14585.cljs$lang$ctorStr = "cljs.core.async/t14585";

cljs.core.async.t14585.cljs$lang$ctorPrWriter = (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"cljs.core.async/t14585");
});

cljs.core.async.__GT_t14585 = (function __GT_t14585(ch__$1,f__$1,map_LT___$1,meta14586){
return (new cljs.core.async.t14585(ch__$1,f__$1,map_LT___$1,meta14586));
});

}

return (new cljs.core.async.t14585(ch,f,map_LT_,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"line","line",212345235),722,new cljs.core.Keyword(null,"column","column",2078222095),3,new cljs.core.Keyword(null,"end-line","end-line",1837326455),743,new cljs.core.Keyword(null,"end-column","end-column",1425389514),46], null)));
});
/**
* Deprecated - this function will be removed. Use transducer instead
*/
cljs.core.async.map_GT_ = (function map_GT_(f,ch){
if(typeof cljs.core.async.t14594 !== 'undefined'){
} else {

/**
* @constructor
*/
cljs.core.async.t14594 = (function (ch,f,map_GT_,meta14595){
this.ch = ch;
this.f = f;
this.map_GT_ = map_GT_;
this.meta14595 = meta14595;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
cljs.core.async.t14594.prototype.cljs$core$async$impl$protocols$WritePort$ = true;

cljs.core.async.t14594.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.put_BANG_.call(null,self__.ch,self__.f.call(null,val),fn1);
});

cljs.core.async.t14594.prototype.cljs$core$async$impl$protocols$ReadPort$ = true;

cljs.core.async.t14594.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.take_BANG_.call(null,self__.ch,fn1);
});

cljs.core.async.t14594.prototype.cljs$core$async$impl$protocols$Channel$ = true;

cljs.core.async.t14594.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_.call(null,self__.ch);
});

cljs.core.async.t14594.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_14596){
var self__ = this;
var _14596__$1 = this;
return self__.meta14595;
});

cljs.core.async.t14594.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_14596,meta14595__$1){
var self__ = this;
var _14596__$1 = this;
return (new cljs.core.async.t14594(self__.ch,self__.f,self__.map_GT_,meta14595__$1));
});

cljs.core.async.t14594.cljs$lang$type = true;

cljs.core.async.t14594.cljs$lang$ctorStr = "cljs.core.async/t14594";

cljs.core.async.t14594.cljs$lang$ctorPrWriter = (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"cljs.core.async/t14594");
});

cljs.core.async.__GT_t14594 = (function __GT_t14594(ch__$1,f__$1,map_GT___$1,meta14595){
return (new cljs.core.async.t14594(ch__$1,f__$1,map_GT___$1,meta14595));
});

}

return (new cljs.core.async.t14594(ch,f,map_GT_,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"line","line",212345235),748,new cljs.core.Keyword(null,"column","column",2078222095),3,new cljs.core.Keyword(null,"end-line","end-line",1837326455),757,new cljs.core.Keyword(null,"end-column","end-column",1425389514),34], null)));
});
/**
* Deprecated - this function will be removed. Use transducer instead
*/
cljs.core.async.filter_GT_ = (function filter_GT_(p,ch){
if(typeof cljs.core.async.t14600 !== 'undefined'){
} else {

/**
* @constructor
*/
cljs.core.async.t14600 = (function (ch,p,filter_GT_,meta14601){
this.ch = ch;
this.p = p;
this.filter_GT_ = filter_GT_;
this.meta14601 = meta14601;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
cljs.core.async.t14600.prototype.cljs$core$async$impl$protocols$WritePort$ = true;

cljs.core.async.t14600.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
if(cljs.core.truth_(self__.p.call(null,val))){
return cljs.core.async.impl.protocols.put_BANG_.call(null,self__.ch,val,fn1);
} else {
return cljs.core.async.impl.channels.box.call(null,cljs.core.not.call(null,cljs.core.async.impl.protocols.closed_QMARK_.call(null,self__.ch)));
}
});

cljs.core.async.t14600.prototype.cljs$core$async$impl$protocols$ReadPort$ = true;

cljs.core.async.t14600.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.take_BANG_.call(null,self__.ch,fn1);
});

cljs.core.async.t14600.prototype.cljs$core$async$impl$protocols$Channel$ = true;

cljs.core.async.t14600.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_.call(null,self__.ch);
});

cljs.core.async.t14600.prototype.cljs$core$async$impl$protocols$Channel$closed_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.closed_QMARK_.call(null,self__.ch);
});

cljs.core.async.t14600.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_14602){
var self__ = this;
var _14602__$1 = this;
return self__.meta14601;
});

cljs.core.async.t14600.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_14602,meta14601__$1){
var self__ = this;
var _14602__$1 = this;
return (new cljs.core.async.t14600(self__.ch,self__.p,self__.filter_GT_,meta14601__$1));
});

cljs.core.async.t14600.cljs$lang$type = true;

cljs.core.async.t14600.cljs$lang$ctorStr = "cljs.core.async/t14600";

cljs.core.async.t14600.cljs$lang$ctorPrWriter = (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"cljs.core.async/t14600");
});

cljs.core.async.__GT_t14600 = (function __GT_t14600(ch__$1,p__$1,filter_GT___$1,meta14601){
return (new cljs.core.async.t14600(ch__$1,p__$1,filter_GT___$1,meta14601));
});

}

return (new cljs.core.async.t14600(ch,p,filter_GT_,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"line","line",212345235),762,new cljs.core.Keyword(null,"column","column",2078222095),3,new cljs.core.Keyword(null,"end-line","end-line",1837326455),774,new cljs.core.Keyword(null,"end-column","end-column",1425389514),48], null)));
});
/**
* Deprecated - this function will be removed. Use transducer instead
*/
cljs.core.async.remove_GT_ = (function remove_GT_(p,ch){
return cljs.core.async.filter_GT_.call(null,cljs.core.complement.call(null,p),ch);
});
/**
* Deprecated - this function will be removed. Use transducer instead
*/
cljs.core.async.filter_LT_ = (function() {
var filter_LT_ = null;
var filter_LT___2 = (function (p,ch){
return filter_LT_.call(null,p,ch,null);
});
var filter_LT___3 = (function (p,ch,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
var c__6505__auto___14685 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___14685,out){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___14685,out){
return (function (state_14664){
var state_val_14665 = (state_14664[(1)]);
if((state_val_14665 === (7))){
var inst_14660 = (state_14664[(2)]);
var state_14664__$1 = state_14664;
var statearr_14666_14686 = state_14664__$1;
(statearr_14666_14686[(2)] = inst_14660);

(statearr_14666_14686[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14665 === (1))){
var state_14664__$1 = state_14664;
var statearr_14667_14687 = state_14664__$1;
(statearr_14667_14687[(2)] = null);

(statearr_14667_14687[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14665 === (4))){
var inst_14646 = (state_14664[(7)]);
var inst_14646__$1 = (state_14664[(2)]);
var inst_14647 = (inst_14646__$1 == null);
var state_14664__$1 = (function (){var statearr_14668 = state_14664;
(statearr_14668[(7)] = inst_14646__$1);

return statearr_14668;
})();
if(cljs.core.truth_(inst_14647)){
var statearr_14669_14688 = state_14664__$1;
(statearr_14669_14688[(1)] = (5));

} else {
var statearr_14670_14689 = state_14664__$1;
(statearr_14670_14689[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14665 === (6))){
var inst_14646 = (state_14664[(7)]);
var inst_14651 = p.call(null,inst_14646);
var state_14664__$1 = state_14664;
if(cljs.core.truth_(inst_14651)){
var statearr_14671_14690 = state_14664__$1;
(statearr_14671_14690[(1)] = (8));

} else {
var statearr_14672_14691 = state_14664__$1;
(statearr_14672_14691[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14665 === (3))){
var inst_14662 = (state_14664[(2)]);
var state_14664__$1 = state_14664;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_14664__$1,inst_14662);
} else {
if((state_val_14665 === (2))){
var state_14664__$1 = state_14664;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_14664__$1,(4),ch);
} else {
if((state_val_14665 === (11))){
var inst_14654 = (state_14664[(2)]);
var state_14664__$1 = state_14664;
var statearr_14673_14692 = state_14664__$1;
(statearr_14673_14692[(2)] = inst_14654);

(statearr_14673_14692[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14665 === (9))){
var state_14664__$1 = state_14664;
var statearr_14674_14693 = state_14664__$1;
(statearr_14674_14693[(2)] = null);

(statearr_14674_14693[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14665 === (5))){
var inst_14649 = cljs.core.async.close_BANG_.call(null,out);
var state_14664__$1 = state_14664;
var statearr_14675_14694 = state_14664__$1;
(statearr_14675_14694[(2)] = inst_14649);

(statearr_14675_14694[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14665 === (10))){
var inst_14657 = (state_14664[(2)]);
var state_14664__$1 = (function (){var statearr_14676 = state_14664;
(statearr_14676[(8)] = inst_14657);

return statearr_14676;
})();
var statearr_14677_14695 = state_14664__$1;
(statearr_14677_14695[(2)] = null);

(statearr_14677_14695[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14665 === (8))){
var inst_14646 = (state_14664[(7)]);
var state_14664__$1 = state_14664;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_14664__$1,(11),out,inst_14646);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___14685,out))
;
return ((function (switch__6490__auto__,c__6505__auto___14685,out){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_14681 = [null,null,null,null,null,null,null,null,null];
(statearr_14681[(0)] = state_machine__6491__auto__);

(statearr_14681[(1)] = (1));

return statearr_14681;
});
var state_machine__6491__auto____1 = (function (state_14664){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_14664);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e14682){if((e14682 instanceof Object)){
var ex__6494__auto__ = e14682;
var statearr_14683_14696 = state_14664;
(statearr_14683_14696[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_14664);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e14682;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__14697 = state_14664;
state_14664 = G__14697;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_14664){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_14664);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___14685,out))
})();
var state__6507__auto__ = (function (){var statearr_14684 = f__6506__auto__.call(null);
(statearr_14684[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___14685);

return statearr_14684;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___14685,out))
);


return out;
});
filter_LT_ = function(p,ch,buf_or_n){
switch(arguments.length){
case 2:
return filter_LT___2.call(this,p,ch);
case 3:
return filter_LT___3.call(this,p,ch,buf_or_n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
filter_LT_.cljs$core$IFn$_invoke$arity$2 = filter_LT___2;
filter_LT_.cljs$core$IFn$_invoke$arity$3 = filter_LT___3;
return filter_LT_;
})()
;
/**
* Deprecated - this function will be removed. Use transducer instead
*/
cljs.core.async.remove_LT_ = (function() {
var remove_LT_ = null;
var remove_LT___2 = (function (p,ch){
return remove_LT_.call(null,p,ch,null);
});
var remove_LT___3 = (function (p,ch,buf_or_n){
return cljs.core.async.filter_LT_.call(null,cljs.core.complement.call(null,p),ch,buf_or_n);
});
remove_LT_ = function(p,ch,buf_or_n){
switch(arguments.length){
case 2:
return remove_LT___2.call(this,p,ch);
case 3:
return remove_LT___3.call(this,p,ch,buf_or_n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
remove_LT_.cljs$core$IFn$_invoke$arity$2 = remove_LT___2;
remove_LT_.cljs$core$IFn$_invoke$arity$3 = remove_LT___3;
return remove_LT_;
})()
;
cljs.core.async.mapcat_STAR_ = (function mapcat_STAR_(f,in$,out){
var c__6505__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto__){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto__){
return (function (state_14863){
var state_val_14864 = (state_14863[(1)]);
if((state_val_14864 === (7))){
var inst_14859 = (state_14863[(2)]);
var state_14863__$1 = state_14863;
var statearr_14865_14906 = state_14863__$1;
(statearr_14865_14906[(2)] = inst_14859);

(statearr_14865_14906[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (20))){
var inst_14829 = (state_14863[(7)]);
var inst_14840 = (state_14863[(2)]);
var inst_14841 = cljs.core.next.call(null,inst_14829);
var inst_14815 = inst_14841;
var inst_14816 = null;
var inst_14817 = (0);
var inst_14818 = (0);
var state_14863__$1 = (function (){var statearr_14866 = state_14863;
(statearr_14866[(8)] = inst_14840);

(statearr_14866[(9)] = inst_14817);

(statearr_14866[(10)] = inst_14815);

(statearr_14866[(11)] = inst_14816);

(statearr_14866[(12)] = inst_14818);

return statearr_14866;
})();
var statearr_14867_14907 = state_14863__$1;
(statearr_14867_14907[(2)] = null);

(statearr_14867_14907[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (1))){
var state_14863__$1 = state_14863;
var statearr_14868_14908 = state_14863__$1;
(statearr_14868_14908[(2)] = null);

(statearr_14868_14908[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (4))){
var inst_14804 = (state_14863[(13)]);
var inst_14804__$1 = (state_14863[(2)]);
var inst_14805 = (inst_14804__$1 == null);
var state_14863__$1 = (function (){var statearr_14869 = state_14863;
(statearr_14869[(13)] = inst_14804__$1);

return statearr_14869;
})();
if(cljs.core.truth_(inst_14805)){
var statearr_14870_14909 = state_14863__$1;
(statearr_14870_14909[(1)] = (5));

} else {
var statearr_14871_14910 = state_14863__$1;
(statearr_14871_14910[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (15))){
var state_14863__$1 = state_14863;
var statearr_14875_14911 = state_14863__$1;
(statearr_14875_14911[(2)] = null);

(statearr_14875_14911[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (21))){
var state_14863__$1 = state_14863;
var statearr_14876_14912 = state_14863__$1;
(statearr_14876_14912[(2)] = null);

(statearr_14876_14912[(1)] = (23));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (13))){
var inst_14817 = (state_14863[(9)]);
var inst_14815 = (state_14863[(10)]);
var inst_14816 = (state_14863[(11)]);
var inst_14818 = (state_14863[(12)]);
var inst_14825 = (state_14863[(2)]);
var inst_14826 = (inst_14818 + (1));
var tmp14872 = inst_14817;
var tmp14873 = inst_14815;
var tmp14874 = inst_14816;
var inst_14815__$1 = tmp14873;
var inst_14816__$1 = tmp14874;
var inst_14817__$1 = tmp14872;
var inst_14818__$1 = inst_14826;
var state_14863__$1 = (function (){var statearr_14877 = state_14863;
(statearr_14877[(14)] = inst_14825);

(statearr_14877[(9)] = inst_14817__$1);

(statearr_14877[(10)] = inst_14815__$1);

(statearr_14877[(11)] = inst_14816__$1);

(statearr_14877[(12)] = inst_14818__$1);

return statearr_14877;
})();
var statearr_14878_14913 = state_14863__$1;
(statearr_14878_14913[(2)] = null);

(statearr_14878_14913[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (22))){
var state_14863__$1 = state_14863;
var statearr_14879_14914 = state_14863__$1;
(statearr_14879_14914[(2)] = null);

(statearr_14879_14914[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (6))){
var inst_14804 = (state_14863[(13)]);
var inst_14813 = f.call(null,inst_14804);
var inst_14814 = cljs.core.seq.call(null,inst_14813);
var inst_14815 = inst_14814;
var inst_14816 = null;
var inst_14817 = (0);
var inst_14818 = (0);
var state_14863__$1 = (function (){var statearr_14880 = state_14863;
(statearr_14880[(9)] = inst_14817);

(statearr_14880[(10)] = inst_14815);

(statearr_14880[(11)] = inst_14816);

(statearr_14880[(12)] = inst_14818);

return statearr_14880;
})();
var statearr_14881_14915 = state_14863__$1;
(statearr_14881_14915[(2)] = null);

(statearr_14881_14915[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (17))){
var inst_14829 = (state_14863[(7)]);
var inst_14833 = cljs.core.chunk_first.call(null,inst_14829);
var inst_14834 = cljs.core.chunk_rest.call(null,inst_14829);
var inst_14835 = cljs.core.count.call(null,inst_14833);
var inst_14815 = inst_14834;
var inst_14816 = inst_14833;
var inst_14817 = inst_14835;
var inst_14818 = (0);
var state_14863__$1 = (function (){var statearr_14882 = state_14863;
(statearr_14882[(9)] = inst_14817);

(statearr_14882[(10)] = inst_14815);

(statearr_14882[(11)] = inst_14816);

(statearr_14882[(12)] = inst_14818);

return statearr_14882;
})();
var statearr_14883_14916 = state_14863__$1;
(statearr_14883_14916[(2)] = null);

(statearr_14883_14916[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (3))){
var inst_14861 = (state_14863[(2)]);
var state_14863__$1 = state_14863;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_14863__$1,inst_14861);
} else {
if((state_val_14864 === (12))){
var inst_14849 = (state_14863[(2)]);
var state_14863__$1 = state_14863;
var statearr_14884_14917 = state_14863__$1;
(statearr_14884_14917[(2)] = inst_14849);

(statearr_14884_14917[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (2))){
var state_14863__$1 = state_14863;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_14863__$1,(4),in$);
} else {
if((state_val_14864 === (23))){
var inst_14857 = (state_14863[(2)]);
var state_14863__$1 = state_14863;
var statearr_14885_14918 = state_14863__$1;
(statearr_14885_14918[(2)] = inst_14857);

(statearr_14885_14918[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (19))){
var inst_14844 = (state_14863[(2)]);
var state_14863__$1 = state_14863;
var statearr_14886_14919 = state_14863__$1;
(statearr_14886_14919[(2)] = inst_14844);

(statearr_14886_14919[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (11))){
var inst_14829 = (state_14863[(7)]);
var inst_14815 = (state_14863[(10)]);
var inst_14829__$1 = cljs.core.seq.call(null,inst_14815);
var state_14863__$1 = (function (){var statearr_14887 = state_14863;
(statearr_14887[(7)] = inst_14829__$1);

return statearr_14887;
})();
if(inst_14829__$1){
var statearr_14888_14920 = state_14863__$1;
(statearr_14888_14920[(1)] = (14));

} else {
var statearr_14889_14921 = state_14863__$1;
(statearr_14889_14921[(1)] = (15));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (9))){
var inst_14851 = (state_14863[(2)]);
var inst_14852 = cljs.core.async.impl.protocols.closed_QMARK_.call(null,out);
var state_14863__$1 = (function (){var statearr_14890 = state_14863;
(statearr_14890[(15)] = inst_14851);

return statearr_14890;
})();
if(cljs.core.truth_(inst_14852)){
var statearr_14891_14922 = state_14863__$1;
(statearr_14891_14922[(1)] = (21));

} else {
var statearr_14892_14923 = state_14863__$1;
(statearr_14892_14923[(1)] = (22));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (5))){
var inst_14807 = cljs.core.async.close_BANG_.call(null,out);
var state_14863__$1 = state_14863;
var statearr_14893_14924 = state_14863__$1;
(statearr_14893_14924[(2)] = inst_14807);

(statearr_14893_14924[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (14))){
var inst_14829 = (state_14863[(7)]);
var inst_14831 = cljs.core.chunked_seq_QMARK_.call(null,inst_14829);
var state_14863__$1 = state_14863;
if(inst_14831){
var statearr_14894_14925 = state_14863__$1;
(statearr_14894_14925[(1)] = (17));

} else {
var statearr_14895_14926 = state_14863__$1;
(statearr_14895_14926[(1)] = (18));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (16))){
var inst_14847 = (state_14863[(2)]);
var state_14863__$1 = state_14863;
var statearr_14896_14927 = state_14863__$1;
(statearr_14896_14927[(2)] = inst_14847);

(statearr_14896_14927[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_14864 === (10))){
var inst_14816 = (state_14863[(11)]);
var inst_14818 = (state_14863[(12)]);
var inst_14823 = cljs.core._nth.call(null,inst_14816,inst_14818);
var state_14863__$1 = state_14863;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_14863__$1,(13),out,inst_14823);
} else {
if((state_val_14864 === (18))){
var inst_14829 = (state_14863[(7)]);
var inst_14838 = cljs.core.first.call(null,inst_14829);
var state_14863__$1 = state_14863;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_14863__$1,(20),out,inst_14838);
} else {
if((state_val_14864 === (8))){
var inst_14817 = (state_14863[(9)]);
var inst_14818 = (state_14863[(12)]);
var inst_14820 = (inst_14818 < inst_14817);
var inst_14821 = inst_14820;
var state_14863__$1 = state_14863;
if(cljs.core.truth_(inst_14821)){
var statearr_14897_14928 = state_14863__$1;
(statearr_14897_14928[(1)] = (10));

} else {
var statearr_14898_14929 = state_14863__$1;
(statearr_14898_14929[(1)] = (11));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto__))
;
return ((function (switch__6490__auto__,c__6505__auto__){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_14902 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_14902[(0)] = state_machine__6491__auto__);

(statearr_14902[(1)] = (1));

return statearr_14902;
});
var state_machine__6491__auto____1 = (function (state_14863){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_14863);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e14903){if((e14903 instanceof Object)){
var ex__6494__auto__ = e14903;
var statearr_14904_14930 = state_14863;
(statearr_14904_14930[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_14863);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e14903;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__14931 = state_14863;
state_14863 = G__14931;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_14863){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_14863);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto__))
})();
var state__6507__auto__ = (function (){var statearr_14905 = f__6506__auto__.call(null);
(statearr_14905[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto__);

return statearr_14905;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto__))
);

return c__6505__auto__;
});
/**
* Deprecated - this function will be removed. Use transducer instead
*/
cljs.core.async.mapcat_LT_ = (function() {
var mapcat_LT_ = null;
var mapcat_LT___2 = (function (f,in$){
return mapcat_LT_.call(null,f,in$,null);
});
var mapcat_LT___3 = (function (f,in$,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
cljs.core.async.mapcat_STAR_.call(null,f,in$,out);

return out;
});
mapcat_LT_ = function(f,in$,buf_or_n){
switch(arguments.length){
case 2:
return mapcat_LT___2.call(this,f,in$);
case 3:
return mapcat_LT___3.call(this,f,in$,buf_or_n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
mapcat_LT_.cljs$core$IFn$_invoke$arity$2 = mapcat_LT___2;
mapcat_LT_.cljs$core$IFn$_invoke$arity$3 = mapcat_LT___3;
return mapcat_LT_;
})()
;
/**
* Deprecated - this function will be removed. Use transducer instead
*/
cljs.core.async.mapcat_GT_ = (function() {
var mapcat_GT_ = null;
var mapcat_GT___2 = (function (f,out){
return mapcat_GT_.call(null,f,out,null);
});
var mapcat_GT___3 = (function (f,out,buf_or_n){
var in$ = cljs.core.async.chan.call(null,buf_or_n);
cljs.core.async.mapcat_STAR_.call(null,f,in$,out);

return in$;
});
mapcat_GT_ = function(f,out,buf_or_n){
switch(arguments.length){
case 2:
return mapcat_GT___2.call(this,f,out);
case 3:
return mapcat_GT___3.call(this,f,out,buf_or_n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
mapcat_GT_.cljs$core$IFn$_invoke$arity$2 = mapcat_GT___2;
mapcat_GT_.cljs$core$IFn$_invoke$arity$3 = mapcat_GT___3;
return mapcat_GT_;
})()
;
/**
* Deprecated - this function will be removed. Use transducer instead
*/
cljs.core.async.unique = (function() {
var unique = null;
var unique__1 = (function (ch){
return unique.call(null,ch,null);
});
var unique__2 = (function (ch,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
var c__6505__auto___15028 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___15028,out){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___15028,out){
return (function (state_15003){
var state_val_15004 = (state_15003[(1)]);
if((state_val_15004 === (7))){
var inst_14998 = (state_15003[(2)]);
var state_15003__$1 = state_15003;
var statearr_15005_15029 = state_15003__$1;
(statearr_15005_15029[(2)] = inst_14998);

(statearr_15005_15029[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15004 === (1))){
var inst_14980 = null;
var state_15003__$1 = (function (){var statearr_15006 = state_15003;
(statearr_15006[(7)] = inst_14980);

return statearr_15006;
})();
var statearr_15007_15030 = state_15003__$1;
(statearr_15007_15030[(2)] = null);

(statearr_15007_15030[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15004 === (4))){
var inst_14983 = (state_15003[(8)]);
var inst_14983__$1 = (state_15003[(2)]);
var inst_14984 = (inst_14983__$1 == null);
var inst_14985 = cljs.core.not.call(null,inst_14984);
var state_15003__$1 = (function (){var statearr_15008 = state_15003;
(statearr_15008[(8)] = inst_14983__$1);

return statearr_15008;
})();
if(inst_14985){
var statearr_15009_15031 = state_15003__$1;
(statearr_15009_15031[(1)] = (5));

} else {
var statearr_15010_15032 = state_15003__$1;
(statearr_15010_15032[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15004 === (6))){
var state_15003__$1 = state_15003;
var statearr_15011_15033 = state_15003__$1;
(statearr_15011_15033[(2)] = null);

(statearr_15011_15033[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15004 === (3))){
var inst_15000 = (state_15003[(2)]);
var inst_15001 = cljs.core.async.close_BANG_.call(null,out);
var state_15003__$1 = (function (){var statearr_15012 = state_15003;
(statearr_15012[(9)] = inst_15000);

return statearr_15012;
})();
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_15003__$1,inst_15001);
} else {
if((state_val_15004 === (2))){
var state_15003__$1 = state_15003;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_15003__$1,(4),ch);
} else {
if((state_val_15004 === (11))){
var inst_14983 = (state_15003[(8)]);
var inst_14992 = (state_15003[(2)]);
var inst_14980 = inst_14983;
var state_15003__$1 = (function (){var statearr_15013 = state_15003;
(statearr_15013[(7)] = inst_14980);

(statearr_15013[(10)] = inst_14992);

return statearr_15013;
})();
var statearr_15014_15034 = state_15003__$1;
(statearr_15014_15034[(2)] = null);

(statearr_15014_15034[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15004 === (9))){
var inst_14983 = (state_15003[(8)]);
var state_15003__$1 = state_15003;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_15003__$1,(11),out,inst_14983);
} else {
if((state_val_15004 === (5))){
var inst_14983 = (state_15003[(8)]);
var inst_14980 = (state_15003[(7)]);
var inst_14987 = cljs.core._EQ_.call(null,inst_14983,inst_14980);
var state_15003__$1 = state_15003;
if(inst_14987){
var statearr_15016_15035 = state_15003__$1;
(statearr_15016_15035[(1)] = (8));

} else {
var statearr_15017_15036 = state_15003__$1;
(statearr_15017_15036[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15004 === (10))){
var inst_14995 = (state_15003[(2)]);
var state_15003__$1 = state_15003;
var statearr_15018_15037 = state_15003__$1;
(statearr_15018_15037[(2)] = inst_14995);

(statearr_15018_15037[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15004 === (8))){
var inst_14980 = (state_15003[(7)]);
var tmp15015 = inst_14980;
var inst_14980__$1 = tmp15015;
var state_15003__$1 = (function (){var statearr_15019 = state_15003;
(statearr_15019[(7)] = inst_14980__$1);

return statearr_15019;
})();
var statearr_15020_15038 = state_15003__$1;
(statearr_15020_15038[(2)] = null);

(statearr_15020_15038[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___15028,out))
;
return ((function (switch__6490__auto__,c__6505__auto___15028,out){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_15024 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_15024[(0)] = state_machine__6491__auto__);

(statearr_15024[(1)] = (1));

return statearr_15024;
});
var state_machine__6491__auto____1 = (function (state_15003){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_15003);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e15025){if((e15025 instanceof Object)){
var ex__6494__auto__ = e15025;
var statearr_15026_15039 = state_15003;
(statearr_15026_15039[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_15003);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e15025;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__15040 = state_15003;
state_15003 = G__15040;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_15003){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_15003);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___15028,out))
})();
var state__6507__auto__ = (function (){var statearr_15027 = f__6506__auto__.call(null);
(statearr_15027[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___15028);

return statearr_15027;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___15028,out))
);


return out;
});
unique = function(ch,buf_or_n){
switch(arguments.length){
case 1:
return unique__1.call(this,ch);
case 2:
return unique__2.call(this,ch,buf_or_n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
unique.cljs$core$IFn$_invoke$arity$1 = unique__1;
unique.cljs$core$IFn$_invoke$arity$2 = unique__2;
return unique;
})()
;
/**
* Deprecated - this function will be removed. Use transducer instead
*/
cljs.core.async.partition = (function() {
var partition = null;
var partition__2 = (function (n,ch){
return partition.call(null,n,ch,null);
});
var partition__3 = (function (n,ch,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
var c__6505__auto___15175 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___15175,out){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___15175,out){
return (function (state_15145){
var state_val_15146 = (state_15145[(1)]);
if((state_val_15146 === (7))){
var inst_15141 = (state_15145[(2)]);
var state_15145__$1 = state_15145;
var statearr_15147_15176 = state_15145__$1;
(statearr_15147_15176[(2)] = inst_15141);

(statearr_15147_15176[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15146 === (1))){
var inst_15108 = (new Array(n));
var inst_15109 = inst_15108;
var inst_15110 = (0);
var state_15145__$1 = (function (){var statearr_15148 = state_15145;
(statearr_15148[(7)] = inst_15110);

(statearr_15148[(8)] = inst_15109);

return statearr_15148;
})();
var statearr_15149_15177 = state_15145__$1;
(statearr_15149_15177[(2)] = null);

(statearr_15149_15177[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15146 === (4))){
var inst_15113 = (state_15145[(9)]);
var inst_15113__$1 = (state_15145[(2)]);
var inst_15114 = (inst_15113__$1 == null);
var inst_15115 = cljs.core.not.call(null,inst_15114);
var state_15145__$1 = (function (){var statearr_15150 = state_15145;
(statearr_15150[(9)] = inst_15113__$1);

return statearr_15150;
})();
if(inst_15115){
var statearr_15151_15178 = state_15145__$1;
(statearr_15151_15178[(1)] = (5));

} else {
var statearr_15152_15179 = state_15145__$1;
(statearr_15152_15179[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15146 === (15))){
var inst_15135 = (state_15145[(2)]);
var state_15145__$1 = state_15145;
var statearr_15153_15180 = state_15145__$1;
(statearr_15153_15180[(2)] = inst_15135);

(statearr_15153_15180[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15146 === (13))){
var state_15145__$1 = state_15145;
var statearr_15154_15181 = state_15145__$1;
(statearr_15154_15181[(2)] = null);

(statearr_15154_15181[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15146 === (6))){
var inst_15110 = (state_15145[(7)]);
var inst_15131 = (inst_15110 > (0));
var state_15145__$1 = state_15145;
if(cljs.core.truth_(inst_15131)){
var statearr_15155_15182 = state_15145__$1;
(statearr_15155_15182[(1)] = (12));

} else {
var statearr_15156_15183 = state_15145__$1;
(statearr_15156_15183[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15146 === (3))){
var inst_15143 = (state_15145[(2)]);
var state_15145__$1 = state_15145;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_15145__$1,inst_15143);
} else {
if((state_val_15146 === (12))){
var inst_15109 = (state_15145[(8)]);
var inst_15133 = cljs.core.vec.call(null,inst_15109);
var state_15145__$1 = state_15145;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_15145__$1,(15),out,inst_15133);
} else {
if((state_val_15146 === (2))){
var state_15145__$1 = state_15145;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_15145__$1,(4),ch);
} else {
if((state_val_15146 === (11))){
var inst_15125 = (state_15145[(2)]);
var inst_15126 = (new Array(n));
var inst_15109 = inst_15126;
var inst_15110 = (0);
var state_15145__$1 = (function (){var statearr_15157 = state_15145;
(statearr_15157[(7)] = inst_15110);

(statearr_15157[(8)] = inst_15109);

(statearr_15157[(10)] = inst_15125);

return statearr_15157;
})();
var statearr_15158_15184 = state_15145__$1;
(statearr_15158_15184[(2)] = null);

(statearr_15158_15184[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15146 === (9))){
var inst_15109 = (state_15145[(8)]);
var inst_15123 = cljs.core.vec.call(null,inst_15109);
var state_15145__$1 = state_15145;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_15145__$1,(11),out,inst_15123);
} else {
if((state_val_15146 === (5))){
var inst_15110 = (state_15145[(7)]);
var inst_15118 = (state_15145[(11)]);
var inst_15109 = (state_15145[(8)]);
var inst_15113 = (state_15145[(9)]);
var inst_15117 = (inst_15109[inst_15110] = inst_15113);
var inst_15118__$1 = (inst_15110 + (1));
var inst_15119 = (inst_15118__$1 < n);
var state_15145__$1 = (function (){var statearr_15159 = state_15145;
(statearr_15159[(11)] = inst_15118__$1);

(statearr_15159[(12)] = inst_15117);

return statearr_15159;
})();
if(cljs.core.truth_(inst_15119)){
var statearr_15160_15185 = state_15145__$1;
(statearr_15160_15185[(1)] = (8));

} else {
var statearr_15161_15186 = state_15145__$1;
(statearr_15161_15186[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15146 === (14))){
var inst_15138 = (state_15145[(2)]);
var inst_15139 = cljs.core.async.close_BANG_.call(null,out);
var state_15145__$1 = (function (){var statearr_15163 = state_15145;
(statearr_15163[(13)] = inst_15138);

return statearr_15163;
})();
var statearr_15164_15187 = state_15145__$1;
(statearr_15164_15187[(2)] = inst_15139);

(statearr_15164_15187[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15146 === (10))){
var inst_15129 = (state_15145[(2)]);
var state_15145__$1 = state_15145;
var statearr_15165_15188 = state_15145__$1;
(statearr_15165_15188[(2)] = inst_15129);

(statearr_15165_15188[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15146 === (8))){
var inst_15118 = (state_15145[(11)]);
var inst_15109 = (state_15145[(8)]);
var tmp15162 = inst_15109;
var inst_15109__$1 = tmp15162;
var inst_15110 = inst_15118;
var state_15145__$1 = (function (){var statearr_15166 = state_15145;
(statearr_15166[(7)] = inst_15110);

(statearr_15166[(8)] = inst_15109__$1);

return statearr_15166;
})();
var statearr_15167_15189 = state_15145__$1;
(statearr_15167_15189[(2)] = null);

(statearr_15167_15189[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___15175,out))
;
return ((function (switch__6490__auto__,c__6505__auto___15175,out){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_15171 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_15171[(0)] = state_machine__6491__auto__);

(statearr_15171[(1)] = (1));

return statearr_15171;
});
var state_machine__6491__auto____1 = (function (state_15145){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_15145);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e15172){if((e15172 instanceof Object)){
var ex__6494__auto__ = e15172;
var statearr_15173_15190 = state_15145;
(statearr_15173_15190[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_15145);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e15172;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__15191 = state_15145;
state_15145 = G__15191;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_15145){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_15145);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___15175,out))
})();
var state__6507__auto__ = (function (){var statearr_15174 = f__6506__auto__.call(null);
(statearr_15174[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___15175);

return statearr_15174;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___15175,out))
);


return out;
});
partition = function(n,ch,buf_or_n){
switch(arguments.length){
case 2:
return partition__2.call(this,n,ch);
case 3:
return partition__3.call(this,n,ch,buf_or_n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
partition.cljs$core$IFn$_invoke$arity$2 = partition__2;
partition.cljs$core$IFn$_invoke$arity$3 = partition__3;
return partition;
})()
;
/**
* Deprecated - this function will be removed. Use transducer instead
*/
cljs.core.async.partition_by = (function() {
var partition_by = null;
var partition_by__2 = (function (f,ch){
return partition_by.call(null,f,ch,null);
});
var partition_by__3 = (function (f,ch,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
var c__6505__auto___15334 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___15334,out){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___15334,out){
return (function (state_15304){
var state_val_15305 = (state_15304[(1)]);
if((state_val_15305 === (7))){
var inst_15300 = (state_15304[(2)]);
var state_15304__$1 = state_15304;
var statearr_15306_15335 = state_15304__$1;
(statearr_15306_15335[(2)] = inst_15300);

(statearr_15306_15335[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15305 === (1))){
var inst_15263 = [];
var inst_15264 = inst_15263;
var inst_15265 = new cljs.core.Keyword("cljs.core.async","nothing","cljs.core.async/nothing",-69252123);
var state_15304__$1 = (function (){var statearr_15307 = state_15304;
(statearr_15307[(7)] = inst_15265);

(statearr_15307[(8)] = inst_15264);

return statearr_15307;
})();
var statearr_15308_15336 = state_15304__$1;
(statearr_15308_15336[(2)] = null);

(statearr_15308_15336[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15305 === (4))){
var inst_15268 = (state_15304[(9)]);
var inst_15268__$1 = (state_15304[(2)]);
var inst_15269 = (inst_15268__$1 == null);
var inst_15270 = cljs.core.not.call(null,inst_15269);
var state_15304__$1 = (function (){var statearr_15309 = state_15304;
(statearr_15309[(9)] = inst_15268__$1);

return statearr_15309;
})();
if(inst_15270){
var statearr_15310_15337 = state_15304__$1;
(statearr_15310_15337[(1)] = (5));

} else {
var statearr_15311_15338 = state_15304__$1;
(statearr_15311_15338[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15305 === (15))){
var inst_15294 = (state_15304[(2)]);
var state_15304__$1 = state_15304;
var statearr_15312_15339 = state_15304__$1;
(statearr_15312_15339[(2)] = inst_15294);

(statearr_15312_15339[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15305 === (13))){
var state_15304__$1 = state_15304;
var statearr_15313_15340 = state_15304__$1;
(statearr_15313_15340[(2)] = null);

(statearr_15313_15340[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15305 === (6))){
var inst_15264 = (state_15304[(8)]);
var inst_15289 = inst_15264.length;
var inst_15290 = (inst_15289 > (0));
var state_15304__$1 = state_15304;
if(cljs.core.truth_(inst_15290)){
var statearr_15314_15341 = state_15304__$1;
(statearr_15314_15341[(1)] = (12));

} else {
var statearr_15315_15342 = state_15304__$1;
(statearr_15315_15342[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15305 === (3))){
var inst_15302 = (state_15304[(2)]);
var state_15304__$1 = state_15304;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_15304__$1,inst_15302);
} else {
if((state_val_15305 === (12))){
var inst_15264 = (state_15304[(8)]);
var inst_15292 = cljs.core.vec.call(null,inst_15264);
var state_15304__$1 = state_15304;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_15304__$1,(15),out,inst_15292);
} else {
if((state_val_15305 === (2))){
var state_15304__$1 = state_15304;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_15304__$1,(4),ch);
} else {
if((state_val_15305 === (11))){
var inst_15272 = (state_15304[(10)]);
var inst_15268 = (state_15304[(9)]);
var inst_15282 = (state_15304[(2)]);
var inst_15283 = [];
var inst_15284 = inst_15283.push(inst_15268);
var inst_15264 = inst_15283;
var inst_15265 = inst_15272;
var state_15304__$1 = (function (){var statearr_15316 = state_15304;
(statearr_15316[(11)] = inst_15282);

(statearr_15316[(7)] = inst_15265);

(statearr_15316[(8)] = inst_15264);

(statearr_15316[(12)] = inst_15284);

return statearr_15316;
})();
var statearr_15317_15343 = state_15304__$1;
(statearr_15317_15343[(2)] = null);

(statearr_15317_15343[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15305 === (9))){
var inst_15264 = (state_15304[(8)]);
var inst_15280 = cljs.core.vec.call(null,inst_15264);
var state_15304__$1 = state_15304;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_15304__$1,(11),out,inst_15280);
} else {
if((state_val_15305 === (5))){
var inst_15272 = (state_15304[(10)]);
var inst_15265 = (state_15304[(7)]);
var inst_15268 = (state_15304[(9)]);
var inst_15272__$1 = f.call(null,inst_15268);
var inst_15273 = cljs.core._EQ_.call(null,inst_15272__$1,inst_15265);
var inst_15274 = cljs.core.keyword_identical_QMARK_.call(null,inst_15265,new cljs.core.Keyword("cljs.core.async","nothing","cljs.core.async/nothing",-69252123));
var inst_15275 = (inst_15273) || (inst_15274);
var state_15304__$1 = (function (){var statearr_15318 = state_15304;
(statearr_15318[(10)] = inst_15272__$1);

return statearr_15318;
})();
if(cljs.core.truth_(inst_15275)){
var statearr_15319_15344 = state_15304__$1;
(statearr_15319_15344[(1)] = (8));

} else {
var statearr_15320_15345 = state_15304__$1;
(statearr_15320_15345[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15305 === (14))){
var inst_15297 = (state_15304[(2)]);
var inst_15298 = cljs.core.async.close_BANG_.call(null,out);
var state_15304__$1 = (function (){var statearr_15322 = state_15304;
(statearr_15322[(13)] = inst_15297);

return statearr_15322;
})();
var statearr_15323_15346 = state_15304__$1;
(statearr_15323_15346[(2)] = inst_15298);

(statearr_15323_15346[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15305 === (10))){
var inst_15287 = (state_15304[(2)]);
var state_15304__$1 = state_15304;
var statearr_15324_15347 = state_15304__$1;
(statearr_15324_15347[(2)] = inst_15287);

(statearr_15324_15347[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_15305 === (8))){
var inst_15272 = (state_15304[(10)]);
var inst_15264 = (state_15304[(8)]);
var inst_15268 = (state_15304[(9)]);
var inst_15277 = inst_15264.push(inst_15268);
var tmp15321 = inst_15264;
var inst_15264__$1 = tmp15321;
var inst_15265 = inst_15272;
var state_15304__$1 = (function (){var statearr_15325 = state_15304;
(statearr_15325[(14)] = inst_15277);

(statearr_15325[(7)] = inst_15265);

(statearr_15325[(8)] = inst_15264__$1);

return statearr_15325;
})();
var statearr_15326_15348 = state_15304__$1;
(statearr_15326_15348[(2)] = null);

(statearr_15326_15348[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});})(c__6505__auto___15334,out))
;
return ((function (switch__6490__auto__,c__6505__auto___15334,out){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_15330 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_15330[(0)] = state_machine__6491__auto__);

(statearr_15330[(1)] = (1));

return statearr_15330;
});
var state_machine__6491__auto____1 = (function (state_15304){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_15304);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e15331){if((e15331 instanceof Object)){
var ex__6494__auto__ = e15331;
var statearr_15332_15349 = state_15304;
(statearr_15332_15349[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_15304);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e15331;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__15350 = state_15304;
state_15304 = G__15350;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_15304){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_15304);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___15334,out))
})();
var state__6507__auto__ = (function (){var statearr_15333 = f__6506__auto__.call(null);
(statearr_15333[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___15334);

return statearr_15333;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___15334,out))
);


return out;
});
partition_by = function(f,ch,buf_or_n){
switch(arguments.length){
case 2:
return partition_by__2.call(this,f,ch);
case 3:
return partition_by__3.call(this,f,ch,buf_or_n);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
partition_by.cljs$core$IFn$_invoke$arity$2 = partition_by__2;
partition_by.cljs$core$IFn$_invoke$arity$3 = partition_by__3;
return partition_by;
})()
;

//# sourceMappingURL=async.js.map