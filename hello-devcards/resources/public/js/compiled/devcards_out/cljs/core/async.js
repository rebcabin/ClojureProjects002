// Compiled by ClojureScript 1.9.229 {}
goog.provide('cljs.core.async');
goog.require('cljs.core');
goog.require('cljs.core.async.impl.channels');
goog.require('cljs.core.async.impl.dispatch');
goog.require('cljs.core.async.impl.ioc_helpers');
goog.require('cljs.core.async.impl.protocols');
goog.require('cljs.core.async.impl.buffers');
goog.require('cljs.core.async.impl.timers');
cljs.core.async.fn_handler = (function cljs$core$async$fn_handler(var_args){
var args29565 = [];
var len__25865__auto___29571 = arguments.length;
var i__25866__auto___29572 = (0);
while(true){
if((i__25866__auto___29572 < len__25865__auto___29571)){
args29565.push((arguments[i__25866__auto___29572]));

var G__29573 = (i__25866__auto___29572 + (1));
i__25866__auto___29572 = G__29573;
continue;
} else {
}
break;
}

var G__29567 = args29565.length;
switch (G__29567) {
case 1:
return cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args29565.length)].join('')));

}
});

cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1 = (function (f){
return cljs.core.async.fn_handler.call(null,f,true);
});

cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2 = (function (f,blockable){
if(typeof cljs.core.async.t_cljs$core$async29568 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async29568 = (function (f,blockable,meta29569){
this.f = f;
this.blockable = blockable;
this.meta29569 = meta29569;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
cljs.core.async.t_cljs$core$async29568.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_29570,meta29569__$1){
var self__ = this;
var _29570__$1 = this;
return (new cljs.core.async.t_cljs$core$async29568(self__.f,self__.blockable,meta29569__$1));
});

cljs.core.async.t_cljs$core$async29568.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_29570){
var self__ = this;
var _29570__$1 = this;
return self__.meta29569;
});

cljs.core.async.t_cljs$core$async29568.prototype.cljs$core$async$impl$protocols$Handler$ = true;

cljs.core.async.t_cljs$core$async29568.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return true;
});

cljs.core.async.t_cljs$core$async29568.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.blockable;
});

cljs.core.async.t_cljs$core$async29568.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.f;
});

cljs.core.async.t_cljs$core$async29568.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"blockable","blockable",-28395259,null),new cljs.core.Symbol(null,"meta29569","meta29569",2121250628,null)], null);
});

cljs.core.async.t_cljs$core$async29568.cljs$lang$type = true;

cljs.core.async.t_cljs$core$async29568.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async29568";

cljs.core.async.t_cljs$core$async29568.cljs$lang$ctorPrWriter = (function (this__25396__auto__,writer__25397__auto__,opt__25398__auto__){
return cljs.core._write.call(null,writer__25397__auto__,"cljs.core.async/t_cljs$core$async29568");
});

cljs.core.async.__GT_t_cljs$core$async29568 = (function cljs$core$async$__GT_t_cljs$core$async29568(f__$1,blockable__$1,meta29569){
return (new cljs.core.async.t_cljs$core$async29568(f__$1,blockable__$1,meta29569));
});

}

return (new cljs.core.async.t_cljs$core$async29568(f,blockable,cljs.core.PersistentArrayMap.EMPTY));
});

cljs.core.async.fn_handler.cljs$lang$maxFixedArity = 2;

/**
 * Returns a fixed buffer of size n. When full, puts will block/park.
 */
cljs.core.async.buffer = (function cljs$core$async$buffer(n){
return cljs.core.async.impl.buffers.fixed_buffer.call(null,n);
});
/**
 * Returns a buffer of size n. When full, puts will complete but
 *   val will be dropped (no transfer).
 */
cljs.core.async.dropping_buffer = (function cljs$core$async$dropping_buffer(n){
return cljs.core.async.impl.buffers.dropping_buffer.call(null,n);
});
/**
 * Returns a buffer of size n. When full, puts will complete, and be
 *   buffered, but oldest elements in buffer will be dropped (not
 *   transferred).
 */
cljs.core.async.sliding_buffer = (function cljs$core$async$sliding_buffer(n){
return cljs.core.async.impl.buffers.sliding_buffer.call(null,n);
});
/**
 * Returns true if a channel created with buff will never block. That is to say,
 * puts into this buffer will never cause the buffer to be full. 
 */
cljs.core.async.unblocking_buffer_QMARK_ = (function cljs$core$async$unblocking_buffer_QMARK_(buff){
if(!((buff == null))){
if((false) || (buff.cljs$core$async$impl$protocols$UnblockingBuffer$)){
return true;
} else {
if((!buff.cljs$lang$protocol_mask$partition$)){
return cljs.core.native_satisfies_QMARK_.call(null,cljs.core.async.impl.protocols.UnblockingBuffer,buff);
} else {
return false;
}
}
} else {
return cljs.core.native_satisfies_QMARK_.call(null,cljs.core.async.impl.protocols.UnblockingBuffer,buff);
}
});
/**
 * Creates a channel with an optional buffer, an optional transducer (like (map f),
 *   (filter p) etc or a composition thereof), and an optional exception handler.
 *   If buf-or-n is a number, will create and use a fixed buffer of that size. If a
 *   transducer is supplied a buffer must be specified. ex-handler must be a
 *   fn of one argument - if an exception occurs during transformation it will be called
 *   with the thrown value as an argument, and any non-nil return value will be placed
 *   in the channel.
 */
cljs.core.async.chan = (function cljs$core$async$chan(var_args){
var args29577 = [];
var len__25865__auto___29580 = arguments.length;
var i__25866__auto___29581 = (0);
while(true){
if((i__25866__auto___29581 < len__25865__auto___29580)){
args29577.push((arguments[i__25866__auto___29581]));

var G__29582 = (i__25866__auto___29581 + (1));
i__25866__auto___29581 = G__29582;
continue;
} else {
}
break;
}

var G__29579 = args29577.length;
switch (G__29579) {
case 0:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args29577.length)].join('')));

}
});

cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0 = (function (){
return cljs.core.async.chan.call(null,null);
});

cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1 = (function (buf_or_n){
return cljs.core.async.chan.call(null,buf_or_n,null,null);
});

cljs.core.async.chan.cljs$core$IFn$_invoke$arity$2 = (function (buf_or_n,xform){
return cljs.core.async.chan.call(null,buf_or_n,xform,null);
});

cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3 = (function (buf_or_n,xform,ex_handler){
var buf_or_n__$1 = ((cljs.core._EQ_.call(null,buf_or_n,(0)))?null:buf_or_n);
if(cljs.core.truth_(xform)){
if(cljs.core.truth_(buf_or_n__$1)){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("buffer must be supplied when transducer is"),cljs.core.str("\n"),cljs.core.str("buf-or-n")].join('')));
}
} else {
}

return cljs.core.async.impl.channels.chan.call(null,((typeof buf_or_n__$1 === 'number')?cljs.core.async.buffer.call(null,buf_or_n__$1):buf_or_n__$1),xform,ex_handler);
});

cljs.core.async.chan.cljs$lang$maxFixedArity = 3;

/**
 * Creates a promise channel with an optional transducer, and an optional
 *   exception-handler. A promise channel can take exactly one value that consumers
 *   will receive. Once full, puts complete but val is dropped (no transfer).
 *   Consumers will block until either a value is placed in the channel or the
 *   channel is closed. See chan for the semantics of xform and ex-handler.
 */
cljs.core.async.promise_chan = (function cljs$core$async$promise_chan(var_args){
var args29584 = [];
var len__25865__auto___29587 = arguments.length;
var i__25866__auto___29588 = (0);
while(true){
if((i__25866__auto___29588 < len__25865__auto___29587)){
args29584.push((arguments[i__25866__auto___29588]));

var G__29589 = (i__25866__auto___29588 + (1));
i__25866__auto___29588 = G__29589;
continue;
} else {
}
break;
}

var G__29586 = args29584.length;
switch (G__29586) {
case 0:
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args29584.length)].join('')));

}
});

cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$0 = (function (){
return cljs.core.async.promise_chan.call(null,null);
});

cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$1 = (function (xform){
return cljs.core.async.promise_chan.call(null,xform,null);
});

cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$2 = (function (xform,ex_handler){
return cljs.core.async.chan.call(null,cljs.core.async.impl.buffers.promise_buffer.call(null),xform,ex_handler);
});

cljs.core.async.promise_chan.cljs$lang$maxFixedArity = 2;

/**
 * Returns a channel that will close after msecs
 */
cljs.core.async.timeout = (function cljs$core$async$timeout(msecs){
return cljs.core.async.impl.timers.timeout.call(null,msecs);
});
/**
 * takes a val from port. Must be called inside a (go ...) block. Will
 *   return nil if closed. Will park if nothing is available.
 *   Returns true unless port is already closed
 */
cljs.core.async._LT__BANG_ = (function cljs$core$async$_LT__BANG_(port){
throw (new Error("<! used not in (go ...) block"));
});
/**
 * Asynchronously takes a val from port, passing to fn1. Will pass nil
 * if closed. If on-caller? (default true) is true, and value is
 * immediately available, will call fn1 on calling thread.
 * Returns nil.
 */
cljs.core.async.take_BANG_ = (function cljs$core$async$take_BANG_(var_args){
var args29591 = [];
var len__25865__auto___29594 = arguments.length;
var i__25866__auto___29595 = (0);
while(true){
if((i__25866__auto___29595 < len__25865__auto___29594)){
args29591.push((arguments[i__25866__auto___29595]));

var G__29596 = (i__25866__auto___29595 + (1));
i__25866__auto___29595 = G__29596;
continue;
} else {
}
break;
}

var G__29593 = args29591.length;
switch (G__29593) {
case 2:
return cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args29591.length)].join('')));

}
});

cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (port,fn1){
return cljs.core.async.take_BANG_.call(null,port,fn1,true);
});

cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (port,fn1,on_caller_QMARK_){
var ret = cljs.core.async.impl.protocols.take_BANG_.call(null,port,cljs.core.async.fn_handler.call(null,fn1));
if(cljs.core.truth_(ret)){
var val_29598 = cljs.core.deref.call(null,ret);
if(cljs.core.truth_(on_caller_QMARK_)){
fn1.call(null,val_29598);
} else {
cljs.core.async.impl.dispatch.run.call(null,((function (val_29598,ret){
return (function (){
return fn1.call(null,val_29598);
});})(val_29598,ret))
);
}
} else {
}

return null;
});

cljs.core.async.take_BANG_.cljs$lang$maxFixedArity = 3;

cljs.core.async.nop = (function cljs$core$async$nop(_){
return null;
});
cljs.core.async.fhnop = cljs.core.async.fn_handler.call(null,cljs.core.async.nop);
/**
 * puts a val into port. nil values are not allowed. Must be called
 *   inside a (go ...) block. Will park if no buffer space is available.
 *   Returns true unless port is already closed.
 */
cljs.core.async._GT__BANG_ = (function cljs$core$async$_GT__BANG_(port,val){
throw (new Error(">! used not in (go ...) block"));
});
/**
 * Asynchronously puts a val into port, calling fn0 (if supplied) when
 * complete. nil values are not allowed. Will throw if closed. If
 * on-caller? (default true) is true, and the put is immediately
 * accepted, will call fn0 on calling thread.  Returns nil.
 */
cljs.core.async.put_BANG_ = (function cljs$core$async$put_BANG_(var_args){
var args29599 = [];
var len__25865__auto___29602 = arguments.length;
var i__25866__auto___29603 = (0);
while(true){
if((i__25866__auto___29603 < len__25865__auto___29602)){
args29599.push((arguments[i__25866__auto___29603]));

var G__29604 = (i__25866__auto___29603 + (1));
i__25866__auto___29603 = G__29604;
continue;
} else {
}
break;
}

var G__29601 = args29599.length;
switch (G__29601) {
case 2:
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args29599.length)].join('')));

}
});

cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (port,val){
var temp__4655__auto__ = cljs.core.async.impl.protocols.put_BANG_.call(null,port,val,cljs.core.async.fhnop);
if(cljs.core.truth_(temp__4655__auto__)){
var ret = temp__4655__auto__;
return cljs.core.deref.call(null,ret);
} else {
return true;
}
});

cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (port,val,fn1){
return cljs.core.async.put_BANG_.call(null,port,val,fn1,true);
});

cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$4 = (function (port,val,fn1,on_caller_QMARK_){
var temp__4655__auto__ = cljs.core.async.impl.protocols.put_BANG_.call(null,port,val,cljs.core.async.fn_handler.call(null,fn1));
if(cljs.core.truth_(temp__4655__auto__)){
var retb = temp__4655__auto__;
var ret = cljs.core.deref.call(null,retb);
if(cljs.core.truth_(on_caller_QMARK_)){
fn1.call(null,ret);
} else {
cljs.core.async.impl.dispatch.run.call(null,((function (ret,retb,temp__4655__auto__){
return (function (){
return fn1.call(null,ret);
});})(ret,retb,temp__4655__auto__))
);
}

return ret;
} else {
return true;
}
});

cljs.core.async.put_BANG_.cljs$lang$maxFixedArity = 4;

cljs.core.async.close_BANG_ = (function cljs$core$async$close_BANG_(port){
return cljs.core.async.impl.protocols.close_BANG_.call(null,port);
});
cljs.core.async.random_array = (function cljs$core$async$random_array(n){
var a = (new Array(n));
var n__25705__auto___29606 = n;
var x_29607 = (0);
while(true){
if((x_29607 < n__25705__auto___29606)){
(a[x_29607] = (0));

var G__29608 = (x_29607 + (1));
x_29607 = G__29608;
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

var G__29609 = (i + (1));
i = G__29609;
continue;
}
break;
}
});
cljs.core.async.alt_flag = (function cljs$core$async$alt_flag(){
var flag = cljs.core.atom.call(null,true);
if(typeof cljs.core.async.t_cljs$core$async29613 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async29613 = (function (alt_flag,flag,meta29614){
this.alt_flag = alt_flag;
this.flag = flag;
this.meta29614 = meta29614;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
cljs.core.async.t_cljs$core$async29613.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (flag){
return (function (_29615,meta29614__$1){
var self__ = this;
var _29615__$1 = this;
return (new cljs.core.async.t_cljs$core$async29613(self__.alt_flag,self__.flag,meta29614__$1));
});})(flag))
;

cljs.core.async.t_cljs$core$async29613.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (flag){
return (function (_29615){
var self__ = this;
var _29615__$1 = this;
return self__.meta29614;
});})(flag))
;

cljs.core.async.t_cljs$core$async29613.prototype.cljs$core$async$impl$protocols$Handler$ = true;

cljs.core.async.t_cljs$core$async29613.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = ((function (flag){
return (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.deref.call(null,self__.flag);
});})(flag))
;

cljs.core.async.t_cljs$core$async29613.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = ((function (flag){
return (function (_){
var self__ = this;
var ___$1 = this;
return true;
});})(flag))
;

cljs.core.async.t_cljs$core$async29613.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = ((function (flag){
return (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_.call(null,self__.flag,null);

return true;
});})(flag))
;

cljs.core.async.t_cljs$core$async29613.getBasis = ((function (flag){
return (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.with_meta(new cljs.core.Symbol(null,"alt-flag","alt-flag",-1794972754,null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"private","private",-558947994),true,new cljs.core.Keyword(null,"arglists","arglists",1661989754),cljs.core.list(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.list(cljs.core.PersistentVector.EMPTY))], null)),new cljs.core.Symbol(null,"flag","flag",-1565787888,null),new cljs.core.Symbol(null,"meta29614","meta29614",213424809,null)], null);
});})(flag))
;

cljs.core.async.t_cljs$core$async29613.cljs$lang$type = true;

cljs.core.async.t_cljs$core$async29613.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async29613";

cljs.core.async.t_cljs$core$async29613.cljs$lang$ctorPrWriter = ((function (flag){
return (function (this__25396__auto__,writer__25397__auto__,opt__25398__auto__){
return cljs.core._write.call(null,writer__25397__auto__,"cljs.core.async/t_cljs$core$async29613");
});})(flag))
;

cljs.core.async.__GT_t_cljs$core$async29613 = ((function (flag){
return (function cljs$core$async$alt_flag_$___GT_t_cljs$core$async29613(alt_flag__$1,flag__$1,meta29614){
return (new cljs.core.async.t_cljs$core$async29613(alt_flag__$1,flag__$1,meta29614));
});})(flag))
;

}

return (new cljs.core.async.t_cljs$core$async29613(cljs$core$async$alt_flag,flag,cljs.core.PersistentArrayMap.EMPTY));
});
cljs.core.async.alt_handler = (function cljs$core$async$alt_handler(flag,cb){
if(typeof cljs.core.async.t_cljs$core$async29619 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async29619 = (function (alt_handler,flag,cb,meta29620){
this.alt_handler = alt_handler;
this.flag = flag;
this.cb = cb;
this.meta29620 = meta29620;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
cljs.core.async.t_cljs$core$async29619.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_29621,meta29620__$1){
var self__ = this;
var _29621__$1 = this;
return (new cljs.core.async.t_cljs$core$async29619(self__.alt_handler,self__.flag,self__.cb,meta29620__$1));
});

cljs.core.async.t_cljs$core$async29619.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_29621){
var self__ = this;
var _29621__$1 = this;
return self__.meta29620;
});

cljs.core.async.t_cljs$core$async29619.prototype.cljs$core$async$impl$protocols$Handler$ = true;

cljs.core.async.t_cljs$core$async29619.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.active_QMARK_.call(null,self__.flag);
});

cljs.core.async.t_cljs$core$async29619.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return true;
});

cljs.core.async.t_cljs$core$async29619.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.async.impl.protocols.commit.call(null,self__.flag);

return self__.cb;
});

cljs.core.async.t_cljs$core$async29619.getBasis = (function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.with_meta(new cljs.core.Symbol(null,"alt-handler","alt-handler",963786170,null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"private","private",-558947994),true,new cljs.core.Keyword(null,"arglists","arglists",1661989754),cljs.core.list(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.list(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"flag","flag",-1565787888,null),new cljs.core.Symbol(null,"cb","cb",-2064487928,null)], null)))], null)),new cljs.core.Symbol(null,"flag","flag",-1565787888,null),new cljs.core.Symbol(null,"cb","cb",-2064487928,null),new cljs.core.Symbol(null,"meta29620","meta29620",-2131685304,null)], null);
});

cljs.core.async.t_cljs$core$async29619.cljs$lang$type = true;

cljs.core.async.t_cljs$core$async29619.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async29619";

cljs.core.async.t_cljs$core$async29619.cljs$lang$ctorPrWriter = (function (this__25396__auto__,writer__25397__auto__,opt__25398__auto__){
return cljs.core._write.call(null,writer__25397__auto__,"cljs.core.async/t_cljs$core$async29619");
});

cljs.core.async.__GT_t_cljs$core$async29619 = (function cljs$core$async$alt_handler_$___GT_t_cljs$core$async29619(alt_handler__$1,flag__$1,cb__$1,meta29620){
return (new cljs.core.async.t_cljs$core$async29619(alt_handler__$1,flag__$1,cb__$1,meta29620));
});

}

return (new cljs.core.async.t_cljs$core$async29619(cljs$core$async$alt_handler,flag,cb,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * returns derefable [val port] if immediate, nil if enqueued
 */
cljs.core.async.do_alts = (function cljs$core$async$do_alts(fret,ports,opts){
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
return (function (p1__29622_SHARP_){
return fret.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [p1__29622_SHARP_,wport], null));
});})(i,val,idx,port,wport,flag,n,idxs,priority))
));
})():cljs.core.async.impl.protocols.take_BANG_.call(null,port,cljs.core.async.alt_handler.call(null,flag,((function (i,idx,port,wport,flag,n,idxs,priority){
return (function (p1__29623_SHARP_){
return fret.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [p1__29623_SHARP_,port], null));
});})(i,idx,port,wport,flag,n,idxs,priority))
)));
if(cljs.core.truth_(vbox)){
return cljs.core.async.impl.channels.box.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.deref.call(null,vbox),(function (){var or__24790__auto__ = wport;
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
return port;
}
})()], null));
} else {
var G__29624 = (i + (1));
i = G__29624;
continue;
}
} else {
return null;
}
break;
}
})();
var or__24790__auto__ = ret;
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
if(cljs.core.contains_QMARK_.call(null,opts,new cljs.core.Keyword(null,"default","default",-1987822328))){
var temp__4657__auto__ = (function (){var and__24778__auto__ = cljs.core.async.impl.protocols.active_QMARK_.call(null,flag);
if(cljs.core.truth_(and__24778__auto__)){
return cljs.core.async.impl.protocols.commit.call(null,flag);
} else {
return and__24778__auto__;
}
})();
if(cljs.core.truth_(temp__4657__auto__)){
var got = temp__4657__auto__;
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
 *   [channel-to-put-to val-to-put], in any combination. Takes will be
 *   made as if by <!, and puts will be made as if by >!. Unless
 *   the :priority option is true, if more than one port operation is
 *   ready a non-deterministic choice will be made. If no operation is
 *   ready and a :default value is supplied, [default-val :default] will
 *   be returned, otherwise alts! will park until the first operation to
 *   become ready completes. Returns [val port] of the completed
 *   operation, where val is the value taken for takes, and a
 *   boolean (true unless already closed, as per put!) for puts.
 * 
 *   opts are passed as :key val ... Supported options:
 * 
 *   :default val - the value to use if none of the operations are immediately ready
 *   :priority true - (default nil) when true, the operations will be tried in order.
 * 
 *   Note: there is no guarantee that the port exps or val exprs will be
 *   used, nor in what order should they be, so they should not be
 *   depended upon for side effects.
 */
cljs.core.async.alts_BANG_ = (function cljs$core$async$alts_BANG_(var_args){
var args__25872__auto__ = [];
var len__25865__auto___29630 = arguments.length;
var i__25866__auto___29631 = (0);
while(true){
if((i__25866__auto___29631 < len__25865__auto___29630)){
args__25872__auto__.push((arguments[i__25866__auto___29631]));

var G__29632 = (i__25866__auto___29631 + (1));
i__25866__auto___29631 = G__29632;
continue;
} else {
}
break;
}

var argseq__25873__auto__ = ((((1) < args__25872__auto__.length))?(new cljs.core.IndexedSeq(args__25872__auto__.slice((1)),(0),null)):null);
return cljs.core.async.alts_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__25873__auto__);
});

cljs.core.async.alts_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (ports,p__29627){
var map__29628 = p__29627;
var map__29628__$1 = ((((!((map__29628 == null)))?((((map__29628.cljs$lang$protocol_mask$partition0$ & (64))) || (map__29628.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__29628):map__29628);
var opts = map__29628__$1;
throw (new Error("alts! used not in (go ...) block"));
});

cljs.core.async.alts_BANG_.cljs$lang$maxFixedArity = (1);

cljs.core.async.alts_BANG_.cljs$lang$applyTo = (function (seq29625){
var G__29626 = cljs.core.first.call(null,seq29625);
var seq29625__$1 = cljs.core.next.call(null,seq29625);
return cljs.core.async.alts_BANG_.cljs$core$IFn$_invoke$arity$variadic(G__29626,seq29625__$1);
});

/**
 * Puts a val into port if it's possible to do so immediately.
 *   nil values are not allowed. Never blocks. Returns true if offer succeeds.
 */
cljs.core.async.offer_BANG_ = (function cljs$core$async$offer_BANG_(port,val){
var ret = cljs.core.async.impl.protocols.put_BANG_.call(null,port,val,cljs.core.async.fn_handler.call(null,cljs.core.async.nop,false));
if(cljs.core.truth_(ret)){
return cljs.core.deref.call(null,ret);
} else {
return null;
}
});
/**
 * Takes a val from port if it's possible to do so immediately.
 *   Never blocks. Returns value if successful, nil otherwise.
 */
cljs.core.async.poll_BANG_ = (function cljs$core$async$poll_BANG_(port){
var ret = cljs.core.async.impl.protocols.take_BANG_.call(null,port,cljs.core.async.fn_handler.call(null,cljs.core.async.nop,false));
if(cljs.core.truth_(ret)){
return cljs.core.deref.call(null,ret);
} else {
return null;
}
});
/**
 * Takes elements from the from channel and supplies them to the to
 * channel. By default, the to channel will be closed when the from
 * channel closes, but can be determined by the close?  parameter. Will
 * stop consuming the from channel if the to channel closes
 */
cljs.core.async.pipe = (function cljs$core$async$pipe(var_args){
var args29633 = [];
var len__25865__auto___29683 = arguments.length;
var i__25866__auto___29684 = (0);
while(true){
if((i__25866__auto___29684 < len__25865__auto___29683)){
args29633.push((arguments[i__25866__auto___29684]));

var G__29685 = (i__25866__auto___29684 + (1));
i__25866__auto___29684 = G__29685;
continue;
} else {
}
break;
}

var G__29635 = args29633.length;
switch (G__29635) {
case 2:
return cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args29633.length)].join('')));

}
});

cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$2 = (function (from,to){
return cljs.core.async.pipe.call(null,from,to,true);
});

cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$3 = (function (from,to,close_QMARK_){
var c__29520__auto___29687 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___29687){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___29687){
return (function (state_29659){
var state_val_29660 = (state_29659[(1)]);
if((state_val_29660 === (7))){
var inst_29655 = (state_29659[(2)]);
var state_29659__$1 = state_29659;
var statearr_29661_29688 = state_29659__$1;
(statearr_29661_29688[(2)] = inst_29655);

(statearr_29661_29688[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29660 === (1))){
var state_29659__$1 = state_29659;
var statearr_29662_29689 = state_29659__$1;
(statearr_29662_29689[(2)] = null);

(statearr_29662_29689[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29660 === (4))){
var inst_29638 = (state_29659[(7)]);
var inst_29638__$1 = (state_29659[(2)]);
var inst_29639 = (inst_29638__$1 == null);
var state_29659__$1 = (function (){var statearr_29663 = state_29659;
(statearr_29663[(7)] = inst_29638__$1);

return statearr_29663;
})();
if(cljs.core.truth_(inst_29639)){
var statearr_29664_29690 = state_29659__$1;
(statearr_29664_29690[(1)] = (5));

} else {
var statearr_29665_29691 = state_29659__$1;
(statearr_29665_29691[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29660 === (13))){
var state_29659__$1 = state_29659;
var statearr_29666_29692 = state_29659__$1;
(statearr_29666_29692[(2)] = null);

(statearr_29666_29692[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29660 === (6))){
var inst_29638 = (state_29659[(7)]);
var state_29659__$1 = state_29659;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_29659__$1,(11),to,inst_29638);
} else {
if((state_val_29660 === (3))){
var inst_29657 = (state_29659[(2)]);
var state_29659__$1 = state_29659;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_29659__$1,inst_29657);
} else {
if((state_val_29660 === (12))){
var state_29659__$1 = state_29659;
var statearr_29667_29693 = state_29659__$1;
(statearr_29667_29693[(2)] = null);

(statearr_29667_29693[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29660 === (2))){
var state_29659__$1 = state_29659;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_29659__$1,(4),from);
} else {
if((state_val_29660 === (11))){
var inst_29648 = (state_29659[(2)]);
var state_29659__$1 = state_29659;
if(cljs.core.truth_(inst_29648)){
var statearr_29668_29694 = state_29659__$1;
(statearr_29668_29694[(1)] = (12));

} else {
var statearr_29669_29695 = state_29659__$1;
(statearr_29669_29695[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29660 === (9))){
var state_29659__$1 = state_29659;
var statearr_29670_29696 = state_29659__$1;
(statearr_29670_29696[(2)] = null);

(statearr_29670_29696[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29660 === (5))){
var state_29659__$1 = state_29659;
if(cljs.core.truth_(close_QMARK_)){
var statearr_29671_29697 = state_29659__$1;
(statearr_29671_29697[(1)] = (8));

} else {
var statearr_29672_29698 = state_29659__$1;
(statearr_29672_29698[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29660 === (14))){
var inst_29653 = (state_29659[(2)]);
var state_29659__$1 = state_29659;
var statearr_29673_29699 = state_29659__$1;
(statearr_29673_29699[(2)] = inst_29653);

(statearr_29673_29699[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29660 === (10))){
var inst_29645 = (state_29659[(2)]);
var state_29659__$1 = state_29659;
var statearr_29674_29700 = state_29659__$1;
(statearr_29674_29700[(2)] = inst_29645);

(statearr_29674_29700[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29660 === (8))){
var inst_29642 = cljs.core.async.close_BANG_.call(null,to);
var state_29659__$1 = state_29659;
var statearr_29675_29701 = state_29659__$1;
(statearr_29675_29701[(2)] = inst_29642);

(statearr_29675_29701[(1)] = (10));


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
});})(c__29520__auto___29687))
;
return ((function (switch__29408__auto__,c__29520__auto___29687){
return (function() {
var cljs$core$async$state_machine__29409__auto__ = null;
var cljs$core$async$state_machine__29409__auto____0 = (function (){
var statearr_29679 = [null,null,null,null,null,null,null,null];
(statearr_29679[(0)] = cljs$core$async$state_machine__29409__auto__);

(statearr_29679[(1)] = (1));

return statearr_29679;
});
var cljs$core$async$state_machine__29409__auto____1 = (function (state_29659){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_29659);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e29680){if((e29680 instanceof Object)){
var ex__29412__auto__ = e29680;
var statearr_29681_29702 = state_29659;
(statearr_29681_29702[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_29659);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e29680;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__29703 = state_29659;
state_29659 = G__29703;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$state_machine__29409__auto__ = function(state_29659){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29409__auto____1.call(this,state_29659);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29409__auto____0;
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29409__auto____1;
return cljs$core$async$state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___29687))
})();
var state__29522__auto__ = (function (){var statearr_29682 = f__29521__auto__.call(null);
(statearr_29682[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___29687);

return statearr_29682;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___29687))
);


return to;
});

cljs.core.async.pipe.cljs$lang$maxFixedArity = 3;

cljs.core.async.pipeline_STAR_ = (function cljs$core$async$pipeline_STAR_(n,to,xf,from,close_QMARK_,ex_handler,type){
if((n > (0))){
} else {
throw (new Error("Assert failed: (pos? n)"));
}

var jobs = cljs.core.async.chan.call(null,n);
var results = cljs.core.async.chan.call(null,n);
var process = ((function (jobs,results){
return (function (p__29891){
var vec__29892 = p__29891;
var v = cljs.core.nth.call(null,vec__29892,(0),null);
var p = cljs.core.nth.call(null,vec__29892,(1),null);
var job = vec__29892;
if((job == null)){
cljs.core.async.close_BANG_.call(null,results);

return null;
} else {
var res = cljs.core.async.chan.call(null,(1),xf,ex_handler);
var c__29520__auto___30078 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___30078,res,vec__29892,v,p,job,jobs,results){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___30078,res,vec__29892,v,p,job,jobs,results){
return (function (state_29899){
var state_val_29900 = (state_29899[(1)]);
if((state_val_29900 === (1))){
var state_29899__$1 = state_29899;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_29899__$1,(2),res,v);
} else {
if((state_val_29900 === (2))){
var inst_29896 = (state_29899[(2)]);
var inst_29897 = cljs.core.async.close_BANG_.call(null,res);
var state_29899__$1 = (function (){var statearr_29901 = state_29899;
(statearr_29901[(7)] = inst_29896);

return statearr_29901;
})();
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_29899__$1,inst_29897);
} else {
return null;
}
}
});})(c__29520__auto___30078,res,vec__29892,v,p,job,jobs,results))
;
return ((function (switch__29408__auto__,c__29520__auto___30078,res,vec__29892,v,p,job,jobs,results){
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0 = (function (){
var statearr_29905 = [null,null,null,null,null,null,null,null];
(statearr_29905[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__);

(statearr_29905[(1)] = (1));

return statearr_29905;
});
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1 = (function (state_29899){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_29899);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e29906){if((e29906 instanceof Object)){
var ex__29412__auto__ = e29906;
var statearr_29907_30079 = state_29899;
(statearr_29907_30079[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_29899);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e29906;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__30080 = state_29899;
state_29899 = G__30080;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__ = function(state_29899){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1.call(this,state_29899);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___30078,res,vec__29892,v,p,job,jobs,results))
})();
var state__29522__auto__ = (function (){var statearr_29908 = f__29521__auto__.call(null);
(statearr_29908[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___30078);

return statearr_29908;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___30078,res,vec__29892,v,p,job,jobs,results))
);


cljs.core.async.put_BANG_.call(null,p,res);

return true;
}
});})(jobs,results))
;
var async = ((function (jobs,results,process){
return (function (p__29909){
var vec__29910 = p__29909;
var v = cljs.core.nth.call(null,vec__29910,(0),null);
var p = cljs.core.nth.call(null,vec__29910,(1),null);
var job = vec__29910;
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
var n__25705__auto___30081 = n;
var __30082 = (0);
while(true){
if((__30082 < n__25705__auto___30081)){
var G__29913_30083 = (((type instanceof cljs.core.Keyword))?type.fqn:null);
switch (G__29913_30083) {
case "compute":
var c__29520__auto___30085 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (__30082,c__29520__auto___30085,G__29913_30083,n__25705__auto___30081,jobs,results,process,async){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (__30082,c__29520__auto___30085,G__29913_30083,n__25705__auto___30081,jobs,results,process,async){
return (function (state_29926){
var state_val_29927 = (state_29926[(1)]);
if((state_val_29927 === (1))){
var state_29926__$1 = state_29926;
var statearr_29928_30086 = state_29926__$1;
(statearr_29928_30086[(2)] = null);

(statearr_29928_30086[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29927 === (2))){
var state_29926__$1 = state_29926;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_29926__$1,(4),jobs);
} else {
if((state_val_29927 === (3))){
var inst_29924 = (state_29926[(2)]);
var state_29926__$1 = state_29926;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_29926__$1,inst_29924);
} else {
if((state_val_29927 === (4))){
var inst_29916 = (state_29926[(2)]);
var inst_29917 = process.call(null,inst_29916);
var state_29926__$1 = state_29926;
if(cljs.core.truth_(inst_29917)){
var statearr_29929_30087 = state_29926__$1;
(statearr_29929_30087[(1)] = (5));

} else {
var statearr_29930_30088 = state_29926__$1;
(statearr_29930_30088[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29927 === (5))){
var state_29926__$1 = state_29926;
var statearr_29931_30089 = state_29926__$1;
(statearr_29931_30089[(2)] = null);

(statearr_29931_30089[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29927 === (6))){
var state_29926__$1 = state_29926;
var statearr_29932_30090 = state_29926__$1;
(statearr_29932_30090[(2)] = null);

(statearr_29932_30090[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29927 === (7))){
var inst_29922 = (state_29926[(2)]);
var state_29926__$1 = state_29926;
var statearr_29933_30091 = state_29926__$1;
(statearr_29933_30091[(2)] = inst_29922);

(statearr_29933_30091[(1)] = (3));


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
});})(__30082,c__29520__auto___30085,G__29913_30083,n__25705__auto___30081,jobs,results,process,async))
;
return ((function (__30082,switch__29408__auto__,c__29520__auto___30085,G__29913_30083,n__25705__auto___30081,jobs,results,process,async){
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0 = (function (){
var statearr_29937 = [null,null,null,null,null,null,null];
(statearr_29937[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__);

(statearr_29937[(1)] = (1));

return statearr_29937;
});
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1 = (function (state_29926){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_29926);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e29938){if((e29938 instanceof Object)){
var ex__29412__auto__ = e29938;
var statearr_29939_30092 = state_29926;
(statearr_29939_30092[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_29926);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e29938;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__30093 = state_29926;
state_29926 = G__30093;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__ = function(state_29926){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1.call(this,state_29926);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__;
})()
;})(__30082,switch__29408__auto__,c__29520__auto___30085,G__29913_30083,n__25705__auto___30081,jobs,results,process,async))
})();
var state__29522__auto__ = (function (){var statearr_29940 = f__29521__auto__.call(null);
(statearr_29940[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___30085);

return statearr_29940;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(__30082,c__29520__auto___30085,G__29913_30083,n__25705__auto___30081,jobs,results,process,async))
);


break;
case "async":
var c__29520__auto___30094 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (__30082,c__29520__auto___30094,G__29913_30083,n__25705__auto___30081,jobs,results,process,async){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (__30082,c__29520__auto___30094,G__29913_30083,n__25705__auto___30081,jobs,results,process,async){
return (function (state_29953){
var state_val_29954 = (state_29953[(1)]);
if((state_val_29954 === (1))){
var state_29953__$1 = state_29953;
var statearr_29955_30095 = state_29953__$1;
(statearr_29955_30095[(2)] = null);

(statearr_29955_30095[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29954 === (2))){
var state_29953__$1 = state_29953;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_29953__$1,(4),jobs);
} else {
if((state_val_29954 === (3))){
var inst_29951 = (state_29953[(2)]);
var state_29953__$1 = state_29953;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_29953__$1,inst_29951);
} else {
if((state_val_29954 === (4))){
var inst_29943 = (state_29953[(2)]);
var inst_29944 = async.call(null,inst_29943);
var state_29953__$1 = state_29953;
if(cljs.core.truth_(inst_29944)){
var statearr_29956_30096 = state_29953__$1;
(statearr_29956_30096[(1)] = (5));

} else {
var statearr_29957_30097 = state_29953__$1;
(statearr_29957_30097[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29954 === (5))){
var state_29953__$1 = state_29953;
var statearr_29958_30098 = state_29953__$1;
(statearr_29958_30098[(2)] = null);

(statearr_29958_30098[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29954 === (6))){
var state_29953__$1 = state_29953;
var statearr_29959_30099 = state_29953__$1;
(statearr_29959_30099[(2)] = null);

(statearr_29959_30099[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29954 === (7))){
var inst_29949 = (state_29953[(2)]);
var state_29953__$1 = state_29953;
var statearr_29960_30100 = state_29953__$1;
(statearr_29960_30100[(2)] = inst_29949);

(statearr_29960_30100[(1)] = (3));


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
});})(__30082,c__29520__auto___30094,G__29913_30083,n__25705__auto___30081,jobs,results,process,async))
;
return ((function (__30082,switch__29408__auto__,c__29520__auto___30094,G__29913_30083,n__25705__auto___30081,jobs,results,process,async){
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0 = (function (){
var statearr_29964 = [null,null,null,null,null,null,null];
(statearr_29964[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__);

(statearr_29964[(1)] = (1));

return statearr_29964;
});
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1 = (function (state_29953){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_29953);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e29965){if((e29965 instanceof Object)){
var ex__29412__auto__ = e29965;
var statearr_29966_30101 = state_29953;
(statearr_29966_30101[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_29953);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e29965;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__30102 = state_29953;
state_29953 = G__30102;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__ = function(state_29953){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1.call(this,state_29953);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__;
})()
;})(__30082,switch__29408__auto__,c__29520__auto___30094,G__29913_30083,n__25705__auto___30081,jobs,results,process,async))
})();
var state__29522__auto__ = (function (){var statearr_29967 = f__29521__auto__.call(null);
(statearr_29967[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___30094);

return statearr_29967;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(__30082,c__29520__auto___30094,G__29913_30083,n__25705__auto___30081,jobs,results,process,async))
);


break;
default:
throw (new Error([cljs.core.str("No matching clause: "),cljs.core.str(type)].join('')));

}

var G__30103 = (__30082 + (1));
__30082 = G__30103;
continue;
} else {
}
break;
}

var c__29520__auto___30104 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___30104,jobs,results,process,async){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___30104,jobs,results,process,async){
return (function (state_29989){
var state_val_29990 = (state_29989[(1)]);
if((state_val_29990 === (1))){
var state_29989__$1 = state_29989;
var statearr_29991_30105 = state_29989__$1;
(statearr_29991_30105[(2)] = null);

(statearr_29991_30105[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29990 === (2))){
var state_29989__$1 = state_29989;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_29989__$1,(4),from);
} else {
if((state_val_29990 === (3))){
var inst_29987 = (state_29989[(2)]);
var state_29989__$1 = state_29989;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_29989__$1,inst_29987);
} else {
if((state_val_29990 === (4))){
var inst_29970 = (state_29989[(7)]);
var inst_29970__$1 = (state_29989[(2)]);
var inst_29971 = (inst_29970__$1 == null);
var state_29989__$1 = (function (){var statearr_29992 = state_29989;
(statearr_29992[(7)] = inst_29970__$1);

return statearr_29992;
})();
if(cljs.core.truth_(inst_29971)){
var statearr_29993_30106 = state_29989__$1;
(statearr_29993_30106[(1)] = (5));

} else {
var statearr_29994_30107 = state_29989__$1;
(statearr_29994_30107[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29990 === (5))){
var inst_29973 = cljs.core.async.close_BANG_.call(null,jobs);
var state_29989__$1 = state_29989;
var statearr_29995_30108 = state_29989__$1;
(statearr_29995_30108[(2)] = inst_29973);

(statearr_29995_30108[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29990 === (6))){
var inst_29970 = (state_29989[(7)]);
var inst_29975 = (state_29989[(8)]);
var inst_29975__$1 = cljs.core.async.chan.call(null,(1));
var inst_29976 = cljs.core.PersistentVector.EMPTY_NODE;
var inst_29977 = [inst_29970,inst_29975__$1];
var inst_29978 = (new cljs.core.PersistentVector(null,2,(5),inst_29976,inst_29977,null));
var state_29989__$1 = (function (){var statearr_29996 = state_29989;
(statearr_29996[(8)] = inst_29975__$1);

return statearr_29996;
})();
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_29989__$1,(8),jobs,inst_29978);
} else {
if((state_val_29990 === (7))){
var inst_29985 = (state_29989[(2)]);
var state_29989__$1 = state_29989;
var statearr_29997_30109 = state_29989__$1;
(statearr_29997_30109[(2)] = inst_29985);

(statearr_29997_30109[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_29990 === (8))){
var inst_29975 = (state_29989[(8)]);
var inst_29980 = (state_29989[(2)]);
var state_29989__$1 = (function (){var statearr_29998 = state_29989;
(statearr_29998[(9)] = inst_29980);

return statearr_29998;
})();
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_29989__$1,(9),results,inst_29975);
} else {
if((state_val_29990 === (9))){
var inst_29982 = (state_29989[(2)]);
var state_29989__$1 = (function (){var statearr_29999 = state_29989;
(statearr_29999[(10)] = inst_29982);

return statearr_29999;
})();
var statearr_30000_30110 = state_29989__$1;
(statearr_30000_30110[(2)] = null);

(statearr_30000_30110[(1)] = (2));


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
});})(c__29520__auto___30104,jobs,results,process,async))
;
return ((function (switch__29408__auto__,c__29520__auto___30104,jobs,results,process,async){
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0 = (function (){
var statearr_30004 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_30004[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__);

(statearr_30004[(1)] = (1));

return statearr_30004;
});
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1 = (function (state_29989){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_29989);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e30005){if((e30005 instanceof Object)){
var ex__29412__auto__ = e30005;
var statearr_30006_30111 = state_29989;
(statearr_30006_30111[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_29989);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e30005;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__30112 = state_29989;
state_29989 = G__30112;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__ = function(state_29989){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1.call(this,state_29989);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___30104,jobs,results,process,async))
})();
var state__29522__auto__ = (function (){var statearr_30007 = f__29521__auto__.call(null);
(statearr_30007[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___30104);

return statearr_30007;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___30104,jobs,results,process,async))
);


var c__29520__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto__,jobs,results,process,async){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto__,jobs,results,process,async){
return (function (state_30045){
var state_val_30046 = (state_30045[(1)]);
if((state_val_30046 === (7))){
var inst_30041 = (state_30045[(2)]);
var state_30045__$1 = state_30045;
var statearr_30047_30113 = state_30045__$1;
(statearr_30047_30113[(2)] = inst_30041);

(statearr_30047_30113[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (20))){
var state_30045__$1 = state_30045;
var statearr_30048_30114 = state_30045__$1;
(statearr_30048_30114[(2)] = null);

(statearr_30048_30114[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (1))){
var state_30045__$1 = state_30045;
var statearr_30049_30115 = state_30045__$1;
(statearr_30049_30115[(2)] = null);

(statearr_30049_30115[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (4))){
var inst_30010 = (state_30045[(7)]);
var inst_30010__$1 = (state_30045[(2)]);
var inst_30011 = (inst_30010__$1 == null);
var state_30045__$1 = (function (){var statearr_30050 = state_30045;
(statearr_30050[(7)] = inst_30010__$1);

return statearr_30050;
})();
if(cljs.core.truth_(inst_30011)){
var statearr_30051_30116 = state_30045__$1;
(statearr_30051_30116[(1)] = (5));

} else {
var statearr_30052_30117 = state_30045__$1;
(statearr_30052_30117[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (15))){
var inst_30023 = (state_30045[(8)]);
var state_30045__$1 = state_30045;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_30045__$1,(18),to,inst_30023);
} else {
if((state_val_30046 === (21))){
var inst_30036 = (state_30045[(2)]);
var state_30045__$1 = state_30045;
var statearr_30053_30118 = state_30045__$1;
(statearr_30053_30118[(2)] = inst_30036);

(statearr_30053_30118[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (13))){
var inst_30038 = (state_30045[(2)]);
var state_30045__$1 = (function (){var statearr_30054 = state_30045;
(statearr_30054[(9)] = inst_30038);

return statearr_30054;
})();
var statearr_30055_30119 = state_30045__$1;
(statearr_30055_30119[(2)] = null);

(statearr_30055_30119[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (6))){
var inst_30010 = (state_30045[(7)]);
var state_30045__$1 = state_30045;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_30045__$1,(11),inst_30010);
} else {
if((state_val_30046 === (17))){
var inst_30031 = (state_30045[(2)]);
var state_30045__$1 = state_30045;
if(cljs.core.truth_(inst_30031)){
var statearr_30056_30120 = state_30045__$1;
(statearr_30056_30120[(1)] = (19));

} else {
var statearr_30057_30121 = state_30045__$1;
(statearr_30057_30121[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (3))){
var inst_30043 = (state_30045[(2)]);
var state_30045__$1 = state_30045;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_30045__$1,inst_30043);
} else {
if((state_val_30046 === (12))){
var inst_30020 = (state_30045[(10)]);
var state_30045__$1 = state_30045;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_30045__$1,(14),inst_30020);
} else {
if((state_val_30046 === (2))){
var state_30045__$1 = state_30045;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_30045__$1,(4),results);
} else {
if((state_val_30046 === (19))){
var state_30045__$1 = state_30045;
var statearr_30058_30122 = state_30045__$1;
(statearr_30058_30122[(2)] = null);

(statearr_30058_30122[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (11))){
var inst_30020 = (state_30045[(2)]);
var state_30045__$1 = (function (){var statearr_30059 = state_30045;
(statearr_30059[(10)] = inst_30020);

return statearr_30059;
})();
var statearr_30060_30123 = state_30045__$1;
(statearr_30060_30123[(2)] = null);

(statearr_30060_30123[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (9))){
var state_30045__$1 = state_30045;
var statearr_30061_30124 = state_30045__$1;
(statearr_30061_30124[(2)] = null);

(statearr_30061_30124[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (5))){
var state_30045__$1 = state_30045;
if(cljs.core.truth_(close_QMARK_)){
var statearr_30062_30125 = state_30045__$1;
(statearr_30062_30125[(1)] = (8));

} else {
var statearr_30063_30126 = state_30045__$1;
(statearr_30063_30126[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (14))){
var inst_30025 = (state_30045[(11)]);
var inst_30023 = (state_30045[(8)]);
var inst_30023__$1 = (state_30045[(2)]);
var inst_30024 = (inst_30023__$1 == null);
var inst_30025__$1 = cljs.core.not.call(null,inst_30024);
var state_30045__$1 = (function (){var statearr_30064 = state_30045;
(statearr_30064[(11)] = inst_30025__$1);

(statearr_30064[(8)] = inst_30023__$1);

return statearr_30064;
})();
if(inst_30025__$1){
var statearr_30065_30127 = state_30045__$1;
(statearr_30065_30127[(1)] = (15));

} else {
var statearr_30066_30128 = state_30045__$1;
(statearr_30066_30128[(1)] = (16));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (16))){
var inst_30025 = (state_30045[(11)]);
var state_30045__$1 = state_30045;
var statearr_30067_30129 = state_30045__$1;
(statearr_30067_30129[(2)] = inst_30025);

(statearr_30067_30129[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (10))){
var inst_30017 = (state_30045[(2)]);
var state_30045__$1 = state_30045;
var statearr_30068_30130 = state_30045__$1;
(statearr_30068_30130[(2)] = inst_30017);

(statearr_30068_30130[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (18))){
var inst_30028 = (state_30045[(2)]);
var state_30045__$1 = state_30045;
var statearr_30069_30131 = state_30045__$1;
(statearr_30069_30131[(2)] = inst_30028);

(statearr_30069_30131[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30046 === (8))){
var inst_30014 = cljs.core.async.close_BANG_.call(null,to);
var state_30045__$1 = state_30045;
var statearr_30070_30132 = state_30045__$1;
(statearr_30070_30132[(2)] = inst_30014);

(statearr_30070_30132[(1)] = (10));


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
});})(c__29520__auto__,jobs,results,process,async))
;
return ((function (switch__29408__auto__,c__29520__auto__,jobs,results,process,async){
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0 = (function (){
var statearr_30074 = [null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_30074[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__);

(statearr_30074[(1)] = (1));

return statearr_30074;
});
var cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1 = (function (state_30045){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_30045);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e30075){if((e30075 instanceof Object)){
var ex__29412__auto__ = e30075;
var statearr_30076_30133 = state_30045;
(statearr_30076_30133[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_30045);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e30075;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__30134 = state_30045;
state_30045 = G__30134;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__ = function(state_30045){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1.call(this,state_30045);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__29409__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto__,jobs,results,process,async))
})();
var state__29522__auto__ = (function (){var statearr_30077 = f__29521__auto__.call(null);
(statearr_30077[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto__);

return statearr_30077;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto__,jobs,results,process,async))
);

return c__29520__auto__;
});
/**
 * Takes elements from the from channel and supplies them to the to
 *   channel, subject to the async function af, with parallelism n. af
 *   must be a function of two arguments, the first an input value and
 *   the second a channel on which to place the result(s). af must close!
 *   the channel before returning.  The presumption is that af will
 *   return immediately, having launched some asynchronous operation
 *   whose completion/callback will manipulate the result channel. Outputs
 *   will be returned in order relative to  the inputs. By default, the to
 *   channel will be closed when the from channel closes, but can be
 *   determined by the close?  parameter. Will stop consuming the from
 *   channel if the to channel closes.
 */
cljs.core.async.pipeline_async = (function cljs$core$async$pipeline_async(var_args){
var args30135 = [];
var len__25865__auto___30138 = arguments.length;
var i__25866__auto___30139 = (0);
while(true){
if((i__25866__auto___30139 < len__25865__auto___30138)){
args30135.push((arguments[i__25866__auto___30139]));

var G__30140 = (i__25866__auto___30139 + (1));
i__25866__auto___30139 = G__30140;
continue;
} else {
}
break;
}

var G__30137 = args30135.length;
switch (G__30137) {
case 4:
return cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30135.length)].join('')));

}
});

cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$4 = (function (n,to,af,from){
return cljs.core.async.pipeline_async.call(null,n,to,af,from,true);
});

cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$5 = (function (n,to,af,from,close_QMARK_){
return cljs.core.async.pipeline_STAR_.call(null,n,to,af,from,close_QMARK_,null,new cljs.core.Keyword(null,"async","async",1050769601));
});

cljs.core.async.pipeline_async.cljs$lang$maxFixedArity = 5;

/**
 * Takes elements from the from channel and supplies them to the to
 *   channel, subject to the transducer xf, with parallelism n. Because
 *   it is parallel, the transducer will be applied independently to each
 *   element, not across elements, and may produce zero or more outputs
 *   per input.  Outputs will be returned in order relative to the
 *   inputs. By default, the to channel will be closed when the from
 *   channel closes, but can be determined by the close?  parameter. Will
 *   stop consuming the from channel if the to channel closes.
 * 
 *   Note this is supplied for API compatibility with the Clojure version.
 *   Values of N > 1 will not result in actual concurrency in a
 *   single-threaded runtime.
 */
cljs.core.async.pipeline = (function cljs$core$async$pipeline(var_args){
var args30142 = [];
var len__25865__auto___30145 = arguments.length;
var i__25866__auto___30146 = (0);
while(true){
if((i__25866__auto___30146 < len__25865__auto___30145)){
args30142.push((arguments[i__25866__auto___30146]));

var G__30147 = (i__25866__auto___30146 + (1));
i__25866__auto___30146 = G__30147;
continue;
} else {
}
break;
}

var G__30144 = args30142.length;
switch (G__30144) {
case 4:
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
case 6:
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$6((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30142.length)].join('')));

}
});

cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$4 = (function (n,to,xf,from){
return cljs.core.async.pipeline.call(null,n,to,xf,from,true);
});

cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$5 = (function (n,to,xf,from,close_QMARK_){
return cljs.core.async.pipeline.call(null,n,to,xf,from,close_QMARK_,null);
});

cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$6 = (function (n,to,xf,from,close_QMARK_,ex_handler){
return cljs.core.async.pipeline_STAR_.call(null,n,to,xf,from,close_QMARK_,ex_handler,new cljs.core.Keyword(null,"compute","compute",1555393130));
});

cljs.core.async.pipeline.cljs$lang$maxFixedArity = 6;

/**
 * Takes a predicate and a source channel and returns a vector of two
 *   channels, the first of which will contain the values for which the
 *   predicate returned true, the second those for which it returned
 *   false.
 * 
 *   The out channels will be unbuffered by default, or two buf-or-ns can
 *   be supplied. The channels will close after the source channel has
 *   closed.
 */
cljs.core.async.split = (function cljs$core$async$split(var_args){
var args30149 = [];
var len__25865__auto___30202 = arguments.length;
var i__25866__auto___30203 = (0);
while(true){
if((i__25866__auto___30203 < len__25865__auto___30202)){
args30149.push((arguments[i__25866__auto___30203]));

var G__30204 = (i__25866__auto___30203 + (1));
i__25866__auto___30203 = G__30204;
continue;
} else {
}
break;
}

var G__30151 = args30149.length;
switch (G__30151) {
case 2:
return cljs.core.async.split.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 4:
return cljs.core.async.split.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30149.length)].join('')));

}
});

cljs.core.async.split.cljs$core$IFn$_invoke$arity$2 = (function (p,ch){
return cljs.core.async.split.call(null,p,ch,null,null);
});

cljs.core.async.split.cljs$core$IFn$_invoke$arity$4 = (function (p,ch,t_buf_or_n,f_buf_or_n){
var tc = cljs.core.async.chan.call(null,t_buf_or_n);
var fc = cljs.core.async.chan.call(null,f_buf_or_n);
var c__29520__auto___30206 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___30206,tc,fc){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___30206,tc,fc){
return (function (state_30177){
var state_val_30178 = (state_30177[(1)]);
if((state_val_30178 === (7))){
var inst_30173 = (state_30177[(2)]);
var state_30177__$1 = state_30177;
var statearr_30179_30207 = state_30177__$1;
(statearr_30179_30207[(2)] = inst_30173);

(statearr_30179_30207[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30178 === (1))){
var state_30177__$1 = state_30177;
var statearr_30180_30208 = state_30177__$1;
(statearr_30180_30208[(2)] = null);

(statearr_30180_30208[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30178 === (4))){
var inst_30154 = (state_30177[(7)]);
var inst_30154__$1 = (state_30177[(2)]);
var inst_30155 = (inst_30154__$1 == null);
var state_30177__$1 = (function (){var statearr_30181 = state_30177;
(statearr_30181[(7)] = inst_30154__$1);

return statearr_30181;
})();
if(cljs.core.truth_(inst_30155)){
var statearr_30182_30209 = state_30177__$1;
(statearr_30182_30209[(1)] = (5));

} else {
var statearr_30183_30210 = state_30177__$1;
(statearr_30183_30210[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30178 === (13))){
var state_30177__$1 = state_30177;
var statearr_30184_30211 = state_30177__$1;
(statearr_30184_30211[(2)] = null);

(statearr_30184_30211[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30178 === (6))){
var inst_30154 = (state_30177[(7)]);
var inst_30160 = p.call(null,inst_30154);
var state_30177__$1 = state_30177;
if(cljs.core.truth_(inst_30160)){
var statearr_30185_30212 = state_30177__$1;
(statearr_30185_30212[(1)] = (9));

} else {
var statearr_30186_30213 = state_30177__$1;
(statearr_30186_30213[(1)] = (10));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30178 === (3))){
var inst_30175 = (state_30177[(2)]);
var state_30177__$1 = state_30177;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_30177__$1,inst_30175);
} else {
if((state_val_30178 === (12))){
var state_30177__$1 = state_30177;
var statearr_30187_30214 = state_30177__$1;
(statearr_30187_30214[(2)] = null);

(statearr_30187_30214[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30178 === (2))){
var state_30177__$1 = state_30177;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_30177__$1,(4),ch);
} else {
if((state_val_30178 === (11))){
var inst_30154 = (state_30177[(7)]);
var inst_30164 = (state_30177[(2)]);
var state_30177__$1 = state_30177;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_30177__$1,(8),inst_30164,inst_30154);
} else {
if((state_val_30178 === (9))){
var state_30177__$1 = state_30177;
var statearr_30188_30215 = state_30177__$1;
(statearr_30188_30215[(2)] = tc);

(statearr_30188_30215[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30178 === (5))){
var inst_30157 = cljs.core.async.close_BANG_.call(null,tc);
var inst_30158 = cljs.core.async.close_BANG_.call(null,fc);
var state_30177__$1 = (function (){var statearr_30189 = state_30177;
(statearr_30189[(8)] = inst_30157);

return statearr_30189;
})();
var statearr_30190_30216 = state_30177__$1;
(statearr_30190_30216[(2)] = inst_30158);

(statearr_30190_30216[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30178 === (14))){
var inst_30171 = (state_30177[(2)]);
var state_30177__$1 = state_30177;
var statearr_30191_30217 = state_30177__$1;
(statearr_30191_30217[(2)] = inst_30171);

(statearr_30191_30217[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30178 === (10))){
var state_30177__$1 = state_30177;
var statearr_30192_30218 = state_30177__$1;
(statearr_30192_30218[(2)] = fc);

(statearr_30192_30218[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30178 === (8))){
var inst_30166 = (state_30177[(2)]);
var state_30177__$1 = state_30177;
if(cljs.core.truth_(inst_30166)){
var statearr_30193_30219 = state_30177__$1;
(statearr_30193_30219[(1)] = (12));

} else {
var statearr_30194_30220 = state_30177__$1;
(statearr_30194_30220[(1)] = (13));

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
});})(c__29520__auto___30206,tc,fc))
;
return ((function (switch__29408__auto__,c__29520__auto___30206,tc,fc){
return (function() {
var cljs$core$async$state_machine__29409__auto__ = null;
var cljs$core$async$state_machine__29409__auto____0 = (function (){
var statearr_30198 = [null,null,null,null,null,null,null,null,null];
(statearr_30198[(0)] = cljs$core$async$state_machine__29409__auto__);

(statearr_30198[(1)] = (1));

return statearr_30198;
});
var cljs$core$async$state_machine__29409__auto____1 = (function (state_30177){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_30177);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e30199){if((e30199 instanceof Object)){
var ex__29412__auto__ = e30199;
var statearr_30200_30221 = state_30177;
(statearr_30200_30221[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_30177);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e30199;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__30222 = state_30177;
state_30177 = G__30222;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$state_machine__29409__auto__ = function(state_30177){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29409__auto____1.call(this,state_30177);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29409__auto____0;
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29409__auto____1;
return cljs$core$async$state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___30206,tc,fc))
})();
var state__29522__auto__ = (function (){var statearr_30201 = f__29521__auto__.call(null);
(statearr_30201[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___30206);

return statearr_30201;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___30206,tc,fc))
);


return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tc,fc], null);
});

cljs.core.async.split.cljs$lang$maxFixedArity = 4;

/**
 * f should be a function of 2 arguments. Returns a channel containing
 *   the single result of applying f to init and the first item from the
 *   channel, then applying f to that result and the 2nd item, etc. If
 *   the channel closes without yielding items, returns init and f is not
 *   called. ch must close before reduce produces a result.
 */
cljs.core.async.reduce = (function cljs$core$async$reduce(f,init,ch){
var c__29520__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto__){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto__){
return (function (state_30286){
var state_val_30287 = (state_30286[(1)]);
if((state_val_30287 === (7))){
var inst_30282 = (state_30286[(2)]);
var state_30286__$1 = state_30286;
var statearr_30288_30309 = state_30286__$1;
(statearr_30288_30309[(2)] = inst_30282);

(statearr_30288_30309[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30287 === (1))){
var inst_30266 = init;
var state_30286__$1 = (function (){var statearr_30289 = state_30286;
(statearr_30289[(7)] = inst_30266);

return statearr_30289;
})();
var statearr_30290_30310 = state_30286__$1;
(statearr_30290_30310[(2)] = null);

(statearr_30290_30310[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30287 === (4))){
var inst_30269 = (state_30286[(8)]);
var inst_30269__$1 = (state_30286[(2)]);
var inst_30270 = (inst_30269__$1 == null);
var state_30286__$1 = (function (){var statearr_30291 = state_30286;
(statearr_30291[(8)] = inst_30269__$1);

return statearr_30291;
})();
if(cljs.core.truth_(inst_30270)){
var statearr_30292_30311 = state_30286__$1;
(statearr_30292_30311[(1)] = (5));

} else {
var statearr_30293_30312 = state_30286__$1;
(statearr_30293_30312[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30287 === (6))){
var inst_30269 = (state_30286[(8)]);
var inst_30266 = (state_30286[(7)]);
var inst_30273 = (state_30286[(9)]);
var inst_30273__$1 = f.call(null,inst_30266,inst_30269);
var inst_30274 = cljs.core.reduced_QMARK_.call(null,inst_30273__$1);
var state_30286__$1 = (function (){var statearr_30294 = state_30286;
(statearr_30294[(9)] = inst_30273__$1);

return statearr_30294;
})();
if(inst_30274){
var statearr_30295_30313 = state_30286__$1;
(statearr_30295_30313[(1)] = (8));

} else {
var statearr_30296_30314 = state_30286__$1;
(statearr_30296_30314[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30287 === (3))){
var inst_30284 = (state_30286[(2)]);
var state_30286__$1 = state_30286;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_30286__$1,inst_30284);
} else {
if((state_val_30287 === (2))){
var state_30286__$1 = state_30286;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_30286__$1,(4),ch);
} else {
if((state_val_30287 === (9))){
var inst_30273 = (state_30286[(9)]);
var inst_30266 = inst_30273;
var state_30286__$1 = (function (){var statearr_30297 = state_30286;
(statearr_30297[(7)] = inst_30266);

return statearr_30297;
})();
var statearr_30298_30315 = state_30286__$1;
(statearr_30298_30315[(2)] = null);

(statearr_30298_30315[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30287 === (5))){
var inst_30266 = (state_30286[(7)]);
var state_30286__$1 = state_30286;
var statearr_30299_30316 = state_30286__$1;
(statearr_30299_30316[(2)] = inst_30266);

(statearr_30299_30316[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30287 === (10))){
var inst_30280 = (state_30286[(2)]);
var state_30286__$1 = state_30286;
var statearr_30300_30317 = state_30286__$1;
(statearr_30300_30317[(2)] = inst_30280);

(statearr_30300_30317[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30287 === (8))){
var inst_30273 = (state_30286[(9)]);
var inst_30276 = cljs.core.deref.call(null,inst_30273);
var state_30286__$1 = state_30286;
var statearr_30301_30318 = state_30286__$1;
(statearr_30301_30318[(2)] = inst_30276);

(statearr_30301_30318[(1)] = (10));


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
});})(c__29520__auto__))
;
return ((function (switch__29408__auto__,c__29520__auto__){
return (function() {
var cljs$core$async$reduce_$_state_machine__29409__auto__ = null;
var cljs$core$async$reduce_$_state_machine__29409__auto____0 = (function (){
var statearr_30305 = [null,null,null,null,null,null,null,null,null,null];
(statearr_30305[(0)] = cljs$core$async$reduce_$_state_machine__29409__auto__);

(statearr_30305[(1)] = (1));

return statearr_30305;
});
var cljs$core$async$reduce_$_state_machine__29409__auto____1 = (function (state_30286){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_30286);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e30306){if((e30306 instanceof Object)){
var ex__29412__auto__ = e30306;
var statearr_30307_30319 = state_30286;
(statearr_30307_30319[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_30286);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e30306;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__30320 = state_30286;
state_30286 = G__30320;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$reduce_$_state_machine__29409__auto__ = function(state_30286){
switch(arguments.length){
case 0:
return cljs$core$async$reduce_$_state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$reduce_$_state_machine__29409__auto____1.call(this,state_30286);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$reduce_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$reduce_$_state_machine__29409__auto____0;
cljs$core$async$reduce_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$reduce_$_state_machine__29409__auto____1;
return cljs$core$async$reduce_$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto__))
})();
var state__29522__auto__ = (function (){var statearr_30308 = f__29521__auto__.call(null);
(statearr_30308[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto__);

return statearr_30308;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto__))
);

return c__29520__auto__;
});
/**
 * Puts the contents of coll into the supplied channel.
 * 
 *   By default the channel will be closed after the items are copied,
 *   but can be determined by the close? parameter.
 * 
 *   Returns a channel which will close after the items are copied.
 */
cljs.core.async.onto_chan = (function cljs$core$async$onto_chan(var_args){
var args30321 = [];
var len__25865__auto___30373 = arguments.length;
var i__25866__auto___30374 = (0);
while(true){
if((i__25866__auto___30374 < len__25865__auto___30373)){
args30321.push((arguments[i__25866__auto___30374]));

var G__30375 = (i__25866__auto___30374 + (1));
i__25866__auto___30374 = G__30375;
continue;
} else {
}
break;
}

var G__30323 = args30321.length;
switch (G__30323) {
case 2:
return cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30321.length)].join('')));

}
});

cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$2 = (function (ch,coll){
return cljs.core.async.onto_chan.call(null,ch,coll,true);
});

cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$3 = (function (ch,coll,close_QMARK_){
var c__29520__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto__){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto__){
return (function (state_30348){
var state_val_30349 = (state_30348[(1)]);
if((state_val_30349 === (7))){
var inst_30330 = (state_30348[(2)]);
var state_30348__$1 = state_30348;
var statearr_30350_30377 = state_30348__$1;
(statearr_30350_30377[(2)] = inst_30330);

(statearr_30350_30377[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30349 === (1))){
var inst_30324 = cljs.core.seq.call(null,coll);
var inst_30325 = inst_30324;
var state_30348__$1 = (function (){var statearr_30351 = state_30348;
(statearr_30351[(7)] = inst_30325);

return statearr_30351;
})();
var statearr_30352_30378 = state_30348__$1;
(statearr_30352_30378[(2)] = null);

(statearr_30352_30378[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30349 === (4))){
var inst_30325 = (state_30348[(7)]);
var inst_30328 = cljs.core.first.call(null,inst_30325);
var state_30348__$1 = state_30348;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_30348__$1,(7),ch,inst_30328);
} else {
if((state_val_30349 === (13))){
var inst_30342 = (state_30348[(2)]);
var state_30348__$1 = state_30348;
var statearr_30353_30379 = state_30348__$1;
(statearr_30353_30379[(2)] = inst_30342);

(statearr_30353_30379[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30349 === (6))){
var inst_30333 = (state_30348[(2)]);
var state_30348__$1 = state_30348;
if(cljs.core.truth_(inst_30333)){
var statearr_30354_30380 = state_30348__$1;
(statearr_30354_30380[(1)] = (8));

} else {
var statearr_30355_30381 = state_30348__$1;
(statearr_30355_30381[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30349 === (3))){
var inst_30346 = (state_30348[(2)]);
var state_30348__$1 = state_30348;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_30348__$1,inst_30346);
} else {
if((state_val_30349 === (12))){
var state_30348__$1 = state_30348;
var statearr_30356_30382 = state_30348__$1;
(statearr_30356_30382[(2)] = null);

(statearr_30356_30382[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30349 === (2))){
var inst_30325 = (state_30348[(7)]);
var state_30348__$1 = state_30348;
if(cljs.core.truth_(inst_30325)){
var statearr_30357_30383 = state_30348__$1;
(statearr_30357_30383[(1)] = (4));

} else {
var statearr_30358_30384 = state_30348__$1;
(statearr_30358_30384[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30349 === (11))){
var inst_30339 = cljs.core.async.close_BANG_.call(null,ch);
var state_30348__$1 = state_30348;
var statearr_30359_30385 = state_30348__$1;
(statearr_30359_30385[(2)] = inst_30339);

(statearr_30359_30385[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30349 === (9))){
var state_30348__$1 = state_30348;
if(cljs.core.truth_(close_QMARK_)){
var statearr_30360_30386 = state_30348__$1;
(statearr_30360_30386[(1)] = (11));

} else {
var statearr_30361_30387 = state_30348__$1;
(statearr_30361_30387[(1)] = (12));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30349 === (5))){
var inst_30325 = (state_30348[(7)]);
var state_30348__$1 = state_30348;
var statearr_30362_30388 = state_30348__$1;
(statearr_30362_30388[(2)] = inst_30325);

(statearr_30362_30388[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30349 === (10))){
var inst_30344 = (state_30348[(2)]);
var state_30348__$1 = state_30348;
var statearr_30363_30389 = state_30348__$1;
(statearr_30363_30389[(2)] = inst_30344);

(statearr_30363_30389[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30349 === (8))){
var inst_30325 = (state_30348[(7)]);
var inst_30335 = cljs.core.next.call(null,inst_30325);
var inst_30325__$1 = inst_30335;
var state_30348__$1 = (function (){var statearr_30364 = state_30348;
(statearr_30364[(7)] = inst_30325__$1);

return statearr_30364;
})();
var statearr_30365_30390 = state_30348__$1;
(statearr_30365_30390[(2)] = null);

(statearr_30365_30390[(1)] = (2));


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
});})(c__29520__auto__))
;
return ((function (switch__29408__auto__,c__29520__auto__){
return (function() {
var cljs$core$async$state_machine__29409__auto__ = null;
var cljs$core$async$state_machine__29409__auto____0 = (function (){
var statearr_30369 = [null,null,null,null,null,null,null,null];
(statearr_30369[(0)] = cljs$core$async$state_machine__29409__auto__);

(statearr_30369[(1)] = (1));

return statearr_30369;
});
var cljs$core$async$state_machine__29409__auto____1 = (function (state_30348){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_30348);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e30370){if((e30370 instanceof Object)){
var ex__29412__auto__ = e30370;
var statearr_30371_30391 = state_30348;
(statearr_30371_30391[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_30348);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e30370;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__30392 = state_30348;
state_30348 = G__30392;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$state_machine__29409__auto__ = function(state_30348){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29409__auto____1.call(this,state_30348);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29409__auto____0;
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29409__auto____1;
return cljs$core$async$state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto__))
})();
var state__29522__auto__ = (function (){var statearr_30372 = f__29521__auto__.call(null);
(statearr_30372[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto__);

return statearr_30372;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto__))
);

return c__29520__auto__;
});

cljs.core.async.onto_chan.cljs$lang$maxFixedArity = 3;

/**
 * Creates and returns a channel which contains the contents of coll,
 *   closing when exhausted.
 */
cljs.core.async.to_chan = (function cljs$core$async$to_chan(coll){
var ch = cljs.core.async.chan.call(null,cljs.core.bounded_count.call(null,(100),coll));
cljs.core.async.onto_chan.call(null,ch,coll);

return ch;
});

/**
 * @interface
 */
cljs.core.async.Mux = function(){};

cljs.core.async.muxch_STAR_ = (function cljs$core$async$muxch_STAR_(_){
if((!((_ == null))) && (!((_.cljs$core$async$Mux$muxch_STAR_$arity$1 == null)))){
return _.cljs$core$async$Mux$muxch_STAR_$arity$1(_);
} else {
var x__25453__auto__ = (((_ == null))?null:_);
var m__25454__auto__ = (cljs.core.async.muxch_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,_);
} else {
var m__25454__auto____$1 = (cljs.core.async.muxch_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,_);
} else {
throw cljs.core.missing_protocol.call(null,"Mux.muxch*",_);
}
}
}
});


/**
 * @interface
 */
cljs.core.async.Mult = function(){};

cljs.core.async.tap_STAR_ = (function cljs$core$async$tap_STAR_(m,ch,close_QMARK_){
if((!((m == null))) && (!((m.cljs$core$async$Mult$tap_STAR_$arity$3 == null)))){
return m.cljs$core$async$Mult$tap_STAR_$arity$3(m,ch,close_QMARK_);
} else {
var x__25453__auto__ = (((m == null))?null:m);
var m__25454__auto__ = (cljs.core.async.tap_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,m,ch,close_QMARK_);
} else {
var m__25454__auto____$1 = (cljs.core.async.tap_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,m,ch,close_QMARK_);
} else {
throw cljs.core.missing_protocol.call(null,"Mult.tap*",m);
}
}
}
});

cljs.core.async.untap_STAR_ = (function cljs$core$async$untap_STAR_(m,ch){
if((!((m == null))) && (!((m.cljs$core$async$Mult$untap_STAR_$arity$2 == null)))){
return m.cljs$core$async$Mult$untap_STAR_$arity$2(m,ch);
} else {
var x__25453__auto__ = (((m == null))?null:m);
var m__25454__auto__ = (cljs.core.async.untap_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,m,ch);
} else {
var m__25454__auto____$1 = (cljs.core.async.untap_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,m,ch);
} else {
throw cljs.core.missing_protocol.call(null,"Mult.untap*",m);
}
}
}
});

cljs.core.async.untap_all_STAR_ = (function cljs$core$async$untap_all_STAR_(m){
if((!((m == null))) && (!((m.cljs$core$async$Mult$untap_all_STAR_$arity$1 == null)))){
return m.cljs$core$async$Mult$untap_all_STAR_$arity$1(m);
} else {
var x__25453__auto__ = (((m == null))?null:m);
var m__25454__auto__ = (cljs.core.async.untap_all_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,m);
} else {
var m__25454__auto____$1 = (cljs.core.async.untap_all_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,m);
} else {
throw cljs.core.missing_protocol.call(null,"Mult.untap-all*",m);
}
}
}
});

/**
 * Creates and returns a mult(iple) of the supplied channel. Channels
 *   containing copies of the channel can be created with 'tap', and
 *   detached with 'untap'.
 * 
 *   Each item is distributed to all taps in parallel and synchronously,
 *   i.e. each tap must accept before the next item is distributed. Use
 *   buffering/windowing to prevent slow taps from holding up the mult.
 * 
 *   Items received when there are no taps get dropped.
 * 
 *   If a tap puts to a closed channel, it will be removed from the mult.
 */
cljs.core.async.mult = (function cljs$core$async$mult(ch){
var cs = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var m = (function (){
if(typeof cljs.core.async.t_cljs$core$async30618 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {cljs.core.async.Mult}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.async.Mux}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async30618 = (function (mult,ch,cs,meta30619){
this.mult = mult;
this.ch = ch;
this.cs = cs;
this.meta30619 = meta30619;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
cljs.core.async.t_cljs$core$async30618.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (cs){
return (function (_30620,meta30619__$1){
var self__ = this;
var _30620__$1 = this;
return (new cljs.core.async.t_cljs$core$async30618(self__.mult,self__.ch,self__.cs,meta30619__$1));
});})(cs))
;

cljs.core.async.t_cljs$core$async30618.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (cs){
return (function (_30620){
var self__ = this;
var _30620__$1 = this;
return self__.meta30619;
});})(cs))
;

cljs.core.async.t_cljs$core$async30618.prototype.cljs$core$async$Mux$ = true;

cljs.core.async.t_cljs$core$async30618.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = ((function (cs){
return (function (_){
var self__ = this;
var ___$1 = this;
return self__.ch;
});})(cs))
;

cljs.core.async.t_cljs$core$async30618.prototype.cljs$core$async$Mult$ = true;

cljs.core.async.t_cljs$core$async30618.prototype.cljs$core$async$Mult$tap_STAR_$arity$3 = ((function (cs){
return (function (_,ch__$1,close_QMARK_){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.call(null,self__.cs,cljs.core.assoc,ch__$1,close_QMARK_);

return null;
});})(cs))
;

cljs.core.async.t_cljs$core$async30618.prototype.cljs$core$async$Mult$untap_STAR_$arity$2 = ((function (cs){
return (function (_,ch__$1){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.call(null,self__.cs,cljs.core.dissoc,ch__$1);

return null;
});})(cs))
;

cljs.core.async.t_cljs$core$async30618.prototype.cljs$core$async$Mult$untap_all_STAR_$arity$1 = ((function (cs){
return (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_.call(null,self__.cs,cljs.core.PersistentArrayMap.EMPTY);

return null;
});})(cs))
;

cljs.core.async.t_cljs$core$async30618.getBasis = ((function (cs){
return (function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.with_meta(new cljs.core.Symbol(null,"mult","mult",-1187640995,null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"arglists","arglists",1661989754),cljs.core.list(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.list(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"ch","ch",1085813622,null)], null))),new cljs.core.Keyword(null,"doc","doc",1913296891),"Creates and returns a mult(iple) of the supplied channel. Channels\n  containing copies of the channel can be created with 'tap', and\n  detached with 'untap'.\n\n  Each item is distributed to all taps in parallel and synchronously,\n  i.e. each tap must accept before the next item is distributed. Use\n  buffering/windowing to prevent slow taps from holding up the mult.\n\n  Items received when there are no taps get dropped.\n\n  If a tap puts to a closed channel, it will be removed from the mult."], null)),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"cs","cs",-117024463,null),new cljs.core.Symbol(null,"meta30619","meta30619",1708456388,null)], null);
});})(cs))
;

cljs.core.async.t_cljs$core$async30618.cljs$lang$type = true;

cljs.core.async.t_cljs$core$async30618.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async30618";

cljs.core.async.t_cljs$core$async30618.cljs$lang$ctorPrWriter = ((function (cs){
return (function (this__25396__auto__,writer__25397__auto__,opt__25398__auto__){
return cljs.core._write.call(null,writer__25397__auto__,"cljs.core.async/t_cljs$core$async30618");
});})(cs))
;

cljs.core.async.__GT_t_cljs$core$async30618 = ((function (cs){
return (function cljs$core$async$mult_$___GT_t_cljs$core$async30618(mult__$1,ch__$1,cs__$1,meta30619){
return (new cljs.core.async.t_cljs$core$async30618(mult__$1,ch__$1,cs__$1,meta30619));
});})(cs))
;

}

return (new cljs.core.async.t_cljs$core$async30618(cljs$core$async$mult,ch,cs,cljs.core.PersistentArrayMap.EMPTY));
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
var c__29520__auto___30843 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___30843,cs,m,dchan,dctr,done){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___30843,cs,m,dchan,dctr,done){
return (function (state_30755){
var state_val_30756 = (state_30755[(1)]);
if((state_val_30756 === (7))){
var inst_30751 = (state_30755[(2)]);
var state_30755__$1 = state_30755;
var statearr_30757_30844 = state_30755__$1;
(statearr_30757_30844[(2)] = inst_30751);

(statearr_30757_30844[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (20))){
var inst_30654 = (state_30755[(7)]);
var inst_30666 = cljs.core.first.call(null,inst_30654);
var inst_30667 = cljs.core.nth.call(null,inst_30666,(0),null);
var inst_30668 = cljs.core.nth.call(null,inst_30666,(1),null);
var state_30755__$1 = (function (){var statearr_30758 = state_30755;
(statearr_30758[(8)] = inst_30667);

return statearr_30758;
})();
if(cljs.core.truth_(inst_30668)){
var statearr_30759_30845 = state_30755__$1;
(statearr_30759_30845[(1)] = (22));

} else {
var statearr_30760_30846 = state_30755__$1;
(statearr_30760_30846[(1)] = (23));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (27))){
var inst_30623 = (state_30755[(9)]);
var inst_30698 = (state_30755[(10)]);
var inst_30696 = (state_30755[(11)]);
var inst_30703 = (state_30755[(12)]);
var inst_30703__$1 = cljs.core._nth.call(null,inst_30696,inst_30698);
var inst_30704 = cljs.core.async.put_BANG_.call(null,inst_30703__$1,inst_30623,done);
var state_30755__$1 = (function (){var statearr_30761 = state_30755;
(statearr_30761[(12)] = inst_30703__$1);

return statearr_30761;
})();
if(cljs.core.truth_(inst_30704)){
var statearr_30762_30847 = state_30755__$1;
(statearr_30762_30847[(1)] = (30));

} else {
var statearr_30763_30848 = state_30755__$1;
(statearr_30763_30848[(1)] = (31));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (1))){
var state_30755__$1 = state_30755;
var statearr_30764_30849 = state_30755__$1;
(statearr_30764_30849[(2)] = null);

(statearr_30764_30849[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (24))){
var inst_30654 = (state_30755[(7)]);
var inst_30673 = (state_30755[(2)]);
var inst_30674 = cljs.core.next.call(null,inst_30654);
var inst_30632 = inst_30674;
var inst_30633 = null;
var inst_30634 = (0);
var inst_30635 = (0);
var state_30755__$1 = (function (){var statearr_30765 = state_30755;
(statearr_30765[(13)] = inst_30632);

(statearr_30765[(14)] = inst_30633);

(statearr_30765[(15)] = inst_30635);

(statearr_30765[(16)] = inst_30673);

(statearr_30765[(17)] = inst_30634);

return statearr_30765;
})();
var statearr_30766_30850 = state_30755__$1;
(statearr_30766_30850[(2)] = null);

(statearr_30766_30850[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (39))){
var state_30755__$1 = state_30755;
var statearr_30770_30851 = state_30755__$1;
(statearr_30770_30851[(2)] = null);

(statearr_30770_30851[(1)] = (41));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (4))){
var inst_30623 = (state_30755[(9)]);
var inst_30623__$1 = (state_30755[(2)]);
var inst_30624 = (inst_30623__$1 == null);
var state_30755__$1 = (function (){var statearr_30771 = state_30755;
(statearr_30771[(9)] = inst_30623__$1);

return statearr_30771;
})();
if(cljs.core.truth_(inst_30624)){
var statearr_30772_30852 = state_30755__$1;
(statearr_30772_30852[(1)] = (5));

} else {
var statearr_30773_30853 = state_30755__$1;
(statearr_30773_30853[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (15))){
var inst_30632 = (state_30755[(13)]);
var inst_30633 = (state_30755[(14)]);
var inst_30635 = (state_30755[(15)]);
var inst_30634 = (state_30755[(17)]);
var inst_30650 = (state_30755[(2)]);
var inst_30651 = (inst_30635 + (1));
var tmp30767 = inst_30632;
var tmp30768 = inst_30633;
var tmp30769 = inst_30634;
var inst_30632__$1 = tmp30767;
var inst_30633__$1 = tmp30768;
var inst_30634__$1 = tmp30769;
var inst_30635__$1 = inst_30651;
var state_30755__$1 = (function (){var statearr_30774 = state_30755;
(statearr_30774[(13)] = inst_30632__$1);

(statearr_30774[(14)] = inst_30633__$1);

(statearr_30774[(15)] = inst_30635__$1);

(statearr_30774[(18)] = inst_30650);

(statearr_30774[(17)] = inst_30634__$1);

return statearr_30774;
})();
var statearr_30775_30854 = state_30755__$1;
(statearr_30775_30854[(2)] = null);

(statearr_30775_30854[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (21))){
var inst_30677 = (state_30755[(2)]);
var state_30755__$1 = state_30755;
var statearr_30779_30855 = state_30755__$1;
(statearr_30779_30855[(2)] = inst_30677);

(statearr_30779_30855[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (31))){
var inst_30703 = (state_30755[(12)]);
var inst_30707 = done.call(null,null);
var inst_30708 = cljs.core.async.untap_STAR_.call(null,m,inst_30703);
var state_30755__$1 = (function (){var statearr_30780 = state_30755;
(statearr_30780[(19)] = inst_30707);

return statearr_30780;
})();
var statearr_30781_30856 = state_30755__$1;
(statearr_30781_30856[(2)] = inst_30708);

(statearr_30781_30856[(1)] = (32));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (32))){
var inst_30697 = (state_30755[(20)]);
var inst_30698 = (state_30755[(10)]);
var inst_30696 = (state_30755[(11)]);
var inst_30695 = (state_30755[(21)]);
var inst_30710 = (state_30755[(2)]);
var inst_30711 = (inst_30698 + (1));
var tmp30776 = inst_30697;
var tmp30777 = inst_30696;
var tmp30778 = inst_30695;
var inst_30695__$1 = tmp30778;
var inst_30696__$1 = tmp30777;
var inst_30697__$1 = tmp30776;
var inst_30698__$1 = inst_30711;
var state_30755__$1 = (function (){var statearr_30782 = state_30755;
(statearr_30782[(20)] = inst_30697__$1);

(statearr_30782[(10)] = inst_30698__$1);

(statearr_30782[(11)] = inst_30696__$1);

(statearr_30782[(22)] = inst_30710);

(statearr_30782[(21)] = inst_30695__$1);

return statearr_30782;
})();
var statearr_30783_30857 = state_30755__$1;
(statearr_30783_30857[(2)] = null);

(statearr_30783_30857[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (40))){
var inst_30723 = (state_30755[(23)]);
var inst_30727 = done.call(null,null);
var inst_30728 = cljs.core.async.untap_STAR_.call(null,m,inst_30723);
var state_30755__$1 = (function (){var statearr_30784 = state_30755;
(statearr_30784[(24)] = inst_30727);

return statearr_30784;
})();
var statearr_30785_30858 = state_30755__$1;
(statearr_30785_30858[(2)] = inst_30728);

(statearr_30785_30858[(1)] = (41));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (33))){
var inst_30714 = (state_30755[(25)]);
var inst_30716 = cljs.core.chunked_seq_QMARK_.call(null,inst_30714);
var state_30755__$1 = state_30755;
if(inst_30716){
var statearr_30786_30859 = state_30755__$1;
(statearr_30786_30859[(1)] = (36));

} else {
var statearr_30787_30860 = state_30755__$1;
(statearr_30787_30860[(1)] = (37));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (13))){
var inst_30644 = (state_30755[(26)]);
var inst_30647 = cljs.core.async.close_BANG_.call(null,inst_30644);
var state_30755__$1 = state_30755;
var statearr_30788_30861 = state_30755__$1;
(statearr_30788_30861[(2)] = inst_30647);

(statearr_30788_30861[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (22))){
var inst_30667 = (state_30755[(8)]);
var inst_30670 = cljs.core.async.close_BANG_.call(null,inst_30667);
var state_30755__$1 = state_30755;
var statearr_30789_30862 = state_30755__$1;
(statearr_30789_30862[(2)] = inst_30670);

(statearr_30789_30862[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (36))){
var inst_30714 = (state_30755[(25)]);
var inst_30718 = cljs.core.chunk_first.call(null,inst_30714);
var inst_30719 = cljs.core.chunk_rest.call(null,inst_30714);
var inst_30720 = cljs.core.count.call(null,inst_30718);
var inst_30695 = inst_30719;
var inst_30696 = inst_30718;
var inst_30697 = inst_30720;
var inst_30698 = (0);
var state_30755__$1 = (function (){var statearr_30790 = state_30755;
(statearr_30790[(20)] = inst_30697);

(statearr_30790[(10)] = inst_30698);

(statearr_30790[(11)] = inst_30696);

(statearr_30790[(21)] = inst_30695);

return statearr_30790;
})();
var statearr_30791_30863 = state_30755__$1;
(statearr_30791_30863[(2)] = null);

(statearr_30791_30863[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (41))){
var inst_30714 = (state_30755[(25)]);
var inst_30730 = (state_30755[(2)]);
var inst_30731 = cljs.core.next.call(null,inst_30714);
var inst_30695 = inst_30731;
var inst_30696 = null;
var inst_30697 = (0);
var inst_30698 = (0);
var state_30755__$1 = (function (){var statearr_30792 = state_30755;
(statearr_30792[(20)] = inst_30697);

(statearr_30792[(10)] = inst_30698);

(statearr_30792[(27)] = inst_30730);

(statearr_30792[(11)] = inst_30696);

(statearr_30792[(21)] = inst_30695);

return statearr_30792;
})();
var statearr_30793_30864 = state_30755__$1;
(statearr_30793_30864[(2)] = null);

(statearr_30793_30864[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (43))){
var state_30755__$1 = state_30755;
var statearr_30794_30865 = state_30755__$1;
(statearr_30794_30865[(2)] = null);

(statearr_30794_30865[(1)] = (44));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (29))){
var inst_30739 = (state_30755[(2)]);
var state_30755__$1 = state_30755;
var statearr_30795_30866 = state_30755__$1;
(statearr_30795_30866[(2)] = inst_30739);

(statearr_30795_30866[(1)] = (26));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (44))){
var inst_30748 = (state_30755[(2)]);
var state_30755__$1 = (function (){var statearr_30796 = state_30755;
(statearr_30796[(28)] = inst_30748);

return statearr_30796;
})();
var statearr_30797_30867 = state_30755__$1;
(statearr_30797_30867[(2)] = null);

(statearr_30797_30867[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (6))){
var inst_30687 = (state_30755[(29)]);
var inst_30686 = cljs.core.deref.call(null,cs);
var inst_30687__$1 = cljs.core.keys.call(null,inst_30686);
var inst_30688 = cljs.core.count.call(null,inst_30687__$1);
var inst_30689 = cljs.core.reset_BANG_.call(null,dctr,inst_30688);
var inst_30694 = cljs.core.seq.call(null,inst_30687__$1);
var inst_30695 = inst_30694;
var inst_30696 = null;
var inst_30697 = (0);
var inst_30698 = (0);
var state_30755__$1 = (function (){var statearr_30798 = state_30755;
(statearr_30798[(20)] = inst_30697);

(statearr_30798[(10)] = inst_30698);

(statearr_30798[(29)] = inst_30687__$1);

(statearr_30798[(30)] = inst_30689);

(statearr_30798[(11)] = inst_30696);

(statearr_30798[(21)] = inst_30695);

return statearr_30798;
})();
var statearr_30799_30868 = state_30755__$1;
(statearr_30799_30868[(2)] = null);

(statearr_30799_30868[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (28))){
var inst_30714 = (state_30755[(25)]);
var inst_30695 = (state_30755[(21)]);
var inst_30714__$1 = cljs.core.seq.call(null,inst_30695);
var state_30755__$1 = (function (){var statearr_30800 = state_30755;
(statearr_30800[(25)] = inst_30714__$1);

return statearr_30800;
})();
if(inst_30714__$1){
var statearr_30801_30869 = state_30755__$1;
(statearr_30801_30869[(1)] = (33));

} else {
var statearr_30802_30870 = state_30755__$1;
(statearr_30802_30870[(1)] = (34));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (25))){
var inst_30697 = (state_30755[(20)]);
var inst_30698 = (state_30755[(10)]);
var inst_30700 = (inst_30698 < inst_30697);
var inst_30701 = inst_30700;
var state_30755__$1 = state_30755;
if(cljs.core.truth_(inst_30701)){
var statearr_30803_30871 = state_30755__$1;
(statearr_30803_30871[(1)] = (27));

} else {
var statearr_30804_30872 = state_30755__$1;
(statearr_30804_30872[(1)] = (28));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (34))){
var state_30755__$1 = state_30755;
var statearr_30805_30873 = state_30755__$1;
(statearr_30805_30873[(2)] = null);

(statearr_30805_30873[(1)] = (35));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (17))){
var state_30755__$1 = state_30755;
var statearr_30806_30874 = state_30755__$1;
(statearr_30806_30874[(2)] = null);

(statearr_30806_30874[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (3))){
var inst_30753 = (state_30755[(2)]);
var state_30755__$1 = state_30755;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_30755__$1,inst_30753);
} else {
if((state_val_30756 === (12))){
var inst_30682 = (state_30755[(2)]);
var state_30755__$1 = state_30755;
var statearr_30807_30875 = state_30755__$1;
(statearr_30807_30875[(2)] = inst_30682);

(statearr_30807_30875[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (2))){
var state_30755__$1 = state_30755;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_30755__$1,(4),ch);
} else {
if((state_val_30756 === (23))){
var state_30755__$1 = state_30755;
var statearr_30808_30876 = state_30755__$1;
(statearr_30808_30876[(2)] = null);

(statearr_30808_30876[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (35))){
var inst_30737 = (state_30755[(2)]);
var state_30755__$1 = state_30755;
var statearr_30809_30877 = state_30755__$1;
(statearr_30809_30877[(2)] = inst_30737);

(statearr_30809_30877[(1)] = (29));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (19))){
var inst_30654 = (state_30755[(7)]);
var inst_30658 = cljs.core.chunk_first.call(null,inst_30654);
var inst_30659 = cljs.core.chunk_rest.call(null,inst_30654);
var inst_30660 = cljs.core.count.call(null,inst_30658);
var inst_30632 = inst_30659;
var inst_30633 = inst_30658;
var inst_30634 = inst_30660;
var inst_30635 = (0);
var state_30755__$1 = (function (){var statearr_30810 = state_30755;
(statearr_30810[(13)] = inst_30632);

(statearr_30810[(14)] = inst_30633);

(statearr_30810[(15)] = inst_30635);

(statearr_30810[(17)] = inst_30634);

return statearr_30810;
})();
var statearr_30811_30878 = state_30755__$1;
(statearr_30811_30878[(2)] = null);

(statearr_30811_30878[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (11))){
var inst_30632 = (state_30755[(13)]);
var inst_30654 = (state_30755[(7)]);
var inst_30654__$1 = cljs.core.seq.call(null,inst_30632);
var state_30755__$1 = (function (){var statearr_30812 = state_30755;
(statearr_30812[(7)] = inst_30654__$1);

return statearr_30812;
})();
if(inst_30654__$1){
var statearr_30813_30879 = state_30755__$1;
(statearr_30813_30879[(1)] = (16));

} else {
var statearr_30814_30880 = state_30755__$1;
(statearr_30814_30880[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (9))){
var inst_30684 = (state_30755[(2)]);
var state_30755__$1 = state_30755;
var statearr_30815_30881 = state_30755__$1;
(statearr_30815_30881[(2)] = inst_30684);

(statearr_30815_30881[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (5))){
var inst_30630 = cljs.core.deref.call(null,cs);
var inst_30631 = cljs.core.seq.call(null,inst_30630);
var inst_30632 = inst_30631;
var inst_30633 = null;
var inst_30634 = (0);
var inst_30635 = (0);
var state_30755__$1 = (function (){var statearr_30816 = state_30755;
(statearr_30816[(13)] = inst_30632);

(statearr_30816[(14)] = inst_30633);

(statearr_30816[(15)] = inst_30635);

(statearr_30816[(17)] = inst_30634);

return statearr_30816;
})();
var statearr_30817_30882 = state_30755__$1;
(statearr_30817_30882[(2)] = null);

(statearr_30817_30882[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (14))){
var state_30755__$1 = state_30755;
var statearr_30818_30883 = state_30755__$1;
(statearr_30818_30883[(2)] = null);

(statearr_30818_30883[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (45))){
var inst_30745 = (state_30755[(2)]);
var state_30755__$1 = state_30755;
var statearr_30819_30884 = state_30755__$1;
(statearr_30819_30884[(2)] = inst_30745);

(statearr_30819_30884[(1)] = (44));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (26))){
var inst_30687 = (state_30755[(29)]);
var inst_30741 = (state_30755[(2)]);
var inst_30742 = cljs.core.seq.call(null,inst_30687);
var state_30755__$1 = (function (){var statearr_30820 = state_30755;
(statearr_30820[(31)] = inst_30741);

return statearr_30820;
})();
if(inst_30742){
var statearr_30821_30885 = state_30755__$1;
(statearr_30821_30885[(1)] = (42));

} else {
var statearr_30822_30886 = state_30755__$1;
(statearr_30822_30886[(1)] = (43));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (16))){
var inst_30654 = (state_30755[(7)]);
var inst_30656 = cljs.core.chunked_seq_QMARK_.call(null,inst_30654);
var state_30755__$1 = state_30755;
if(inst_30656){
var statearr_30823_30887 = state_30755__$1;
(statearr_30823_30887[(1)] = (19));

} else {
var statearr_30824_30888 = state_30755__$1;
(statearr_30824_30888[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (38))){
var inst_30734 = (state_30755[(2)]);
var state_30755__$1 = state_30755;
var statearr_30825_30889 = state_30755__$1;
(statearr_30825_30889[(2)] = inst_30734);

(statearr_30825_30889[(1)] = (35));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (30))){
var state_30755__$1 = state_30755;
var statearr_30826_30890 = state_30755__$1;
(statearr_30826_30890[(2)] = null);

(statearr_30826_30890[(1)] = (32));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (10))){
var inst_30633 = (state_30755[(14)]);
var inst_30635 = (state_30755[(15)]);
var inst_30643 = cljs.core._nth.call(null,inst_30633,inst_30635);
var inst_30644 = cljs.core.nth.call(null,inst_30643,(0),null);
var inst_30645 = cljs.core.nth.call(null,inst_30643,(1),null);
var state_30755__$1 = (function (){var statearr_30827 = state_30755;
(statearr_30827[(26)] = inst_30644);

return statearr_30827;
})();
if(cljs.core.truth_(inst_30645)){
var statearr_30828_30891 = state_30755__$1;
(statearr_30828_30891[(1)] = (13));

} else {
var statearr_30829_30892 = state_30755__$1;
(statearr_30829_30892[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (18))){
var inst_30680 = (state_30755[(2)]);
var state_30755__$1 = state_30755;
var statearr_30830_30893 = state_30755__$1;
(statearr_30830_30893[(2)] = inst_30680);

(statearr_30830_30893[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (42))){
var state_30755__$1 = state_30755;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_30755__$1,(45),dchan);
} else {
if((state_val_30756 === (37))){
var inst_30623 = (state_30755[(9)]);
var inst_30714 = (state_30755[(25)]);
var inst_30723 = (state_30755[(23)]);
var inst_30723__$1 = cljs.core.first.call(null,inst_30714);
var inst_30724 = cljs.core.async.put_BANG_.call(null,inst_30723__$1,inst_30623,done);
var state_30755__$1 = (function (){var statearr_30831 = state_30755;
(statearr_30831[(23)] = inst_30723__$1);

return statearr_30831;
})();
if(cljs.core.truth_(inst_30724)){
var statearr_30832_30894 = state_30755__$1;
(statearr_30832_30894[(1)] = (39));

} else {
var statearr_30833_30895 = state_30755__$1;
(statearr_30833_30895[(1)] = (40));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30756 === (8))){
var inst_30635 = (state_30755[(15)]);
var inst_30634 = (state_30755[(17)]);
var inst_30637 = (inst_30635 < inst_30634);
var inst_30638 = inst_30637;
var state_30755__$1 = state_30755;
if(cljs.core.truth_(inst_30638)){
var statearr_30834_30896 = state_30755__$1;
(statearr_30834_30896[(1)] = (10));

} else {
var statearr_30835_30897 = state_30755__$1;
(statearr_30835_30897[(1)] = (11));

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
});})(c__29520__auto___30843,cs,m,dchan,dctr,done))
;
return ((function (switch__29408__auto__,c__29520__auto___30843,cs,m,dchan,dctr,done){
return (function() {
var cljs$core$async$mult_$_state_machine__29409__auto__ = null;
var cljs$core$async$mult_$_state_machine__29409__auto____0 = (function (){
var statearr_30839 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_30839[(0)] = cljs$core$async$mult_$_state_machine__29409__auto__);

(statearr_30839[(1)] = (1));

return statearr_30839;
});
var cljs$core$async$mult_$_state_machine__29409__auto____1 = (function (state_30755){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_30755);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e30840){if((e30840 instanceof Object)){
var ex__29412__auto__ = e30840;
var statearr_30841_30898 = state_30755;
(statearr_30841_30898[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_30755);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e30840;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__30899 = state_30755;
state_30755 = G__30899;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$mult_$_state_machine__29409__auto__ = function(state_30755){
switch(arguments.length){
case 0:
return cljs$core$async$mult_$_state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$mult_$_state_machine__29409__auto____1.call(this,state_30755);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$mult_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$mult_$_state_machine__29409__auto____0;
cljs$core$async$mult_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$mult_$_state_machine__29409__auto____1;
return cljs$core$async$mult_$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___30843,cs,m,dchan,dctr,done))
})();
var state__29522__auto__ = (function (){var statearr_30842 = f__29521__auto__.call(null);
(statearr_30842[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___30843);

return statearr_30842;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___30843,cs,m,dchan,dctr,done))
);


return m;
});
/**
 * Copies the mult source onto the supplied channel.
 * 
 *   By default the channel will be closed when the source closes,
 *   but can be determined by the close? parameter.
 */
cljs.core.async.tap = (function cljs$core$async$tap(var_args){
var args30900 = [];
var len__25865__auto___30903 = arguments.length;
var i__25866__auto___30904 = (0);
while(true){
if((i__25866__auto___30904 < len__25865__auto___30903)){
args30900.push((arguments[i__25866__auto___30904]));

var G__30905 = (i__25866__auto___30904 + (1));
i__25866__auto___30904 = G__30905;
continue;
} else {
}
break;
}

var G__30902 = args30900.length;
switch (G__30902) {
case 2:
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30900.length)].join('')));

}
});

cljs.core.async.tap.cljs$core$IFn$_invoke$arity$2 = (function (mult,ch){
return cljs.core.async.tap.call(null,mult,ch,true);
});

cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3 = (function (mult,ch,close_QMARK_){
cljs.core.async.tap_STAR_.call(null,mult,ch,close_QMARK_);

return ch;
});

cljs.core.async.tap.cljs$lang$maxFixedArity = 3;

/**
 * Disconnects a target channel from a mult
 */
cljs.core.async.untap = (function cljs$core$async$untap(mult,ch){
return cljs.core.async.untap_STAR_.call(null,mult,ch);
});
/**
 * Disconnects all target channels from a mult
 */
cljs.core.async.untap_all = (function cljs$core$async$untap_all(mult){
return cljs.core.async.untap_all_STAR_.call(null,mult);
});

/**
 * @interface
 */
cljs.core.async.Mix = function(){};

cljs.core.async.admix_STAR_ = (function cljs$core$async$admix_STAR_(m,ch){
if((!((m == null))) && (!((m.cljs$core$async$Mix$admix_STAR_$arity$2 == null)))){
return m.cljs$core$async$Mix$admix_STAR_$arity$2(m,ch);
} else {
var x__25453__auto__ = (((m == null))?null:m);
var m__25454__auto__ = (cljs.core.async.admix_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,m,ch);
} else {
var m__25454__auto____$1 = (cljs.core.async.admix_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,m,ch);
} else {
throw cljs.core.missing_protocol.call(null,"Mix.admix*",m);
}
}
}
});

cljs.core.async.unmix_STAR_ = (function cljs$core$async$unmix_STAR_(m,ch){
if((!((m == null))) && (!((m.cljs$core$async$Mix$unmix_STAR_$arity$2 == null)))){
return m.cljs$core$async$Mix$unmix_STAR_$arity$2(m,ch);
} else {
var x__25453__auto__ = (((m == null))?null:m);
var m__25454__auto__ = (cljs.core.async.unmix_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,m,ch);
} else {
var m__25454__auto____$1 = (cljs.core.async.unmix_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,m,ch);
} else {
throw cljs.core.missing_protocol.call(null,"Mix.unmix*",m);
}
}
}
});

cljs.core.async.unmix_all_STAR_ = (function cljs$core$async$unmix_all_STAR_(m){
if((!((m == null))) && (!((m.cljs$core$async$Mix$unmix_all_STAR_$arity$1 == null)))){
return m.cljs$core$async$Mix$unmix_all_STAR_$arity$1(m);
} else {
var x__25453__auto__ = (((m == null))?null:m);
var m__25454__auto__ = (cljs.core.async.unmix_all_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,m);
} else {
var m__25454__auto____$1 = (cljs.core.async.unmix_all_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,m);
} else {
throw cljs.core.missing_protocol.call(null,"Mix.unmix-all*",m);
}
}
}
});

cljs.core.async.toggle_STAR_ = (function cljs$core$async$toggle_STAR_(m,state_map){
if((!((m == null))) && (!((m.cljs$core$async$Mix$toggle_STAR_$arity$2 == null)))){
return m.cljs$core$async$Mix$toggle_STAR_$arity$2(m,state_map);
} else {
var x__25453__auto__ = (((m == null))?null:m);
var m__25454__auto__ = (cljs.core.async.toggle_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,m,state_map);
} else {
var m__25454__auto____$1 = (cljs.core.async.toggle_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,m,state_map);
} else {
throw cljs.core.missing_protocol.call(null,"Mix.toggle*",m);
}
}
}
});

cljs.core.async.solo_mode_STAR_ = (function cljs$core$async$solo_mode_STAR_(m,mode){
if((!((m == null))) && (!((m.cljs$core$async$Mix$solo_mode_STAR_$arity$2 == null)))){
return m.cljs$core$async$Mix$solo_mode_STAR_$arity$2(m,mode);
} else {
var x__25453__auto__ = (((m == null))?null:m);
var m__25454__auto__ = (cljs.core.async.solo_mode_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,m,mode);
} else {
var m__25454__auto____$1 = (cljs.core.async.solo_mode_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,m,mode);
} else {
throw cljs.core.missing_protocol.call(null,"Mix.solo-mode*",m);
}
}
}
});

cljs.core.async.ioc_alts_BANG_ = (function cljs$core$async$ioc_alts_BANG_(var_args){
var args__25872__auto__ = [];
var len__25865__auto___30917 = arguments.length;
var i__25866__auto___30918 = (0);
while(true){
if((i__25866__auto___30918 < len__25865__auto___30917)){
args__25872__auto__.push((arguments[i__25866__auto___30918]));

var G__30919 = (i__25866__auto___30918 + (1));
i__25866__auto___30918 = G__30919;
continue;
} else {
}
break;
}

var argseq__25873__auto__ = ((((3) < args__25872__auto__.length))?(new cljs.core.IndexedSeq(args__25872__auto__.slice((3)),(0),null)):null);
return cljs.core.async.ioc_alts_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),argseq__25873__auto__);
});

cljs.core.async.ioc_alts_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (state,cont_block,ports,p__30911){
var map__30912 = p__30911;
var map__30912__$1 = ((((!((map__30912 == null)))?((((map__30912.cljs$lang$protocol_mask$partition0$ & (64))) || (map__30912.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__30912):map__30912);
var opts = map__30912__$1;
var statearr_30914_30920 = state;
(statearr_30914_30920[cljs.core.async.impl.ioc_helpers.STATE_IDX] = cont_block);


var temp__4657__auto__ = cljs.core.async.do_alts.call(null,((function (map__30912,map__30912__$1,opts){
return (function (val){
var statearr_30915_30921 = state;
(statearr_30915_30921[cljs.core.async.impl.ioc_helpers.VALUE_IDX] = val);


return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state);
});})(map__30912,map__30912__$1,opts))
,ports,opts);
if(cljs.core.truth_(temp__4657__auto__)){
var cb = temp__4657__auto__;
var statearr_30916_30922 = state;
(statearr_30916_30922[cljs.core.async.impl.ioc_helpers.VALUE_IDX] = cljs.core.deref.call(null,cb));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
});

cljs.core.async.ioc_alts_BANG_.cljs$lang$maxFixedArity = (3);

cljs.core.async.ioc_alts_BANG_.cljs$lang$applyTo = (function (seq30907){
var G__30908 = cljs.core.first.call(null,seq30907);
var seq30907__$1 = cljs.core.next.call(null,seq30907);
var G__30909 = cljs.core.first.call(null,seq30907__$1);
var seq30907__$2 = cljs.core.next.call(null,seq30907__$1);
var G__30910 = cljs.core.first.call(null,seq30907__$2);
var seq30907__$3 = cljs.core.next.call(null,seq30907__$2);
return cljs.core.async.ioc_alts_BANG_.cljs$core$IFn$_invoke$arity$variadic(G__30908,G__30909,G__30910,seq30907__$3);
});

/**
 * Creates and returns a mix of one or more input channels which will
 *   be put on the supplied out channel. Input sources can be added to
 *   the mix with 'admix', and removed with 'unmix'. A mix supports
 *   soloing, muting and pausing multiple inputs atomically using
 *   'toggle', and can solo using either muting or pausing as determined
 *   by 'solo-mode'.
 * 
 *   Each channel can have zero or more boolean modes set via 'toggle':
 * 
 *   :solo - when true, only this (ond other soloed) channel(s) will appear
 *        in the mix output channel. :mute and :pause states of soloed
 *        channels are ignored. If solo-mode is :mute, non-soloed
 *        channels are muted, if :pause, non-soloed channels are
 *        paused.
 * 
 *   :mute - muted channels will have their contents consumed but not included in the mix
 *   :pause - paused channels will not have their contents consumed (and thus also not included in the mix)
 */
cljs.core.async.mix = (function cljs$core$async$mix(out){
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
if(typeof cljs.core.async.t_cljs$core$async31088 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.async.Mix}
 * @implements {cljs.core.async.Mux}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async31088 = (function (change,mix,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,meta31089){
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
this.meta31089 = meta31089;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
cljs.core.async.t_cljs$core$async31088.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_31090,meta31089__$1){
var self__ = this;
var _31090__$1 = this;
return (new cljs.core.async.t_cljs$core$async31088(self__.change,self__.mix,self__.solo_mode,self__.pick,self__.cs,self__.calc_state,self__.out,self__.changed,self__.solo_modes,self__.attrs,meta31089__$1));
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t_cljs$core$async31088.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_31090){
var self__ = this;
var _31090__$1 = this;
return self__.meta31089;
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t_cljs$core$async31088.prototype.cljs$core$async$Mux$ = true;

cljs.core.async.t_cljs$core$async31088.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_){
var self__ = this;
var ___$1 = this;
return self__.out;
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t_cljs$core$async31088.prototype.cljs$core$async$Mix$ = true;

cljs.core.async.t_cljs$core$async31088.prototype.cljs$core$async$Mix$admix_STAR_$arity$2 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_,ch){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.call(null,self__.cs,cljs.core.assoc,ch,cljs.core.PersistentArrayMap.EMPTY);

return self__.changed.call(null);
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t_cljs$core$async31088.prototype.cljs$core$async$Mix$unmix_STAR_$arity$2 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_,ch){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.call(null,self__.cs,cljs.core.dissoc,ch);

return self__.changed.call(null);
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t_cljs$core$async31088.prototype.cljs$core$async$Mix$unmix_all_STAR_$arity$1 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_.call(null,self__.cs,cljs.core.PersistentArrayMap.EMPTY);

return self__.changed.call(null);
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t_cljs$core$async31088.prototype.cljs$core$async$Mix$toggle_STAR_$arity$2 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_,state_map){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.call(null,self__.cs,cljs.core.partial.call(null,cljs.core.merge_with,cljs.core.merge),state_map);

return self__.changed.call(null);
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t_cljs$core$async31088.prototype.cljs$core$async$Mix$solo_mode_STAR_$arity$2 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (_,mode){
var self__ = this;
var ___$1 = this;
if(cljs.core.truth_(self__.solo_modes.call(null,mode))){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str([cljs.core.str("mode must be one of: "),cljs.core.str(self__.solo_modes)].join('')),cljs.core.str("\n"),cljs.core.str("(solo-modes mode)")].join('')));
}

cljs.core.reset_BANG_.call(null,self__.solo_mode,mode);

return self__.changed.call(null);
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t_cljs$core$async31088.getBasis = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (){
return new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"change","change",477485025,null),cljs.core.with_meta(new cljs.core.Symbol(null,"mix","mix",2121373763,null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"arglists","arglists",1661989754),cljs.core.list(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.list(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"out","out",729986010,null)], null))),new cljs.core.Keyword(null,"doc","doc",1913296891),"Creates and returns a mix of one or more input channels which will\n  be put on the supplied out channel. Input sources can be added to\n  the mix with 'admix', and removed with 'unmix'. A mix supports\n  soloing, muting and pausing multiple inputs atomically using\n  'toggle', and can solo using either muting or pausing as determined\n  by 'solo-mode'.\n\n  Each channel can have zero or more boolean modes set via 'toggle':\n\n  :solo - when true, only this (ond other soloed) channel(s) will appear\n          in the mix output channel. :mute and :pause states of soloed\n          channels are ignored. If solo-mode is :mute, non-soloed\n          channels are muted, if :pause, non-soloed channels are\n          paused.\n\n  :mute - muted channels will have their contents consumed but not included in the mix\n  :pause - paused channels will not have their contents consumed (and thus also not included in the mix)\n"], null)),new cljs.core.Symbol(null,"solo-mode","solo-mode",2031788074,null),new cljs.core.Symbol(null,"pick","pick",1300068175,null),new cljs.core.Symbol(null,"cs","cs",-117024463,null),new cljs.core.Symbol(null,"calc-state","calc-state",-349968968,null),new cljs.core.Symbol(null,"out","out",729986010,null),new cljs.core.Symbol(null,"changed","changed",-2083710852,null),new cljs.core.Symbol(null,"solo-modes","solo-modes",882180540,null),new cljs.core.Symbol(null,"attrs","attrs",-450137186,null),new cljs.core.Symbol(null,"meta31089","meta31089",675819406,null)], null);
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.t_cljs$core$async31088.cljs$lang$type = true;

cljs.core.async.t_cljs$core$async31088.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async31088";

cljs.core.async.t_cljs$core$async31088.cljs$lang$ctorPrWriter = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function (this__25396__auto__,writer__25397__auto__,opt__25398__auto__){
return cljs.core._write.call(null,writer__25397__auto__,"cljs.core.async/t_cljs$core$async31088");
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

cljs.core.async.__GT_t_cljs$core$async31088 = ((function (cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state){
return (function cljs$core$async$mix_$___GT_t_cljs$core$async31088(change__$1,mix__$1,solo_mode__$1,pick__$1,cs__$1,calc_state__$1,out__$1,changed__$1,solo_modes__$1,attrs__$1,meta31089){
return (new cljs.core.async.t_cljs$core$async31088(change__$1,mix__$1,solo_mode__$1,pick__$1,cs__$1,calc_state__$1,out__$1,changed__$1,solo_modes__$1,attrs__$1,meta31089));
});})(cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state))
;

}

return (new cljs.core.async.t_cljs$core$async31088(change,cljs$core$async$mix,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,cljs.core.PersistentArrayMap.EMPTY));
})()
;
var c__29520__auto___31253 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___31253,cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state,m){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___31253,cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state,m){
return (function (state_31190){
var state_val_31191 = (state_31190[(1)]);
if((state_val_31191 === (7))){
var inst_31106 = (state_31190[(2)]);
var state_31190__$1 = state_31190;
var statearr_31192_31254 = state_31190__$1;
(statearr_31192_31254[(2)] = inst_31106);

(statearr_31192_31254[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (20))){
var inst_31118 = (state_31190[(7)]);
var state_31190__$1 = state_31190;
var statearr_31193_31255 = state_31190__$1;
(statearr_31193_31255[(2)] = inst_31118);

(statearr_31193_31255[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (27))){
var state_31190__$1 = state_31190;
var statearr_31194_31256 = state_31190__$1;
(statearr_31194_31256[(2)] = null);

(statearr_31194_31256[(1)] = (28));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (1))){
var inst_31094 = (state_31190[(8)]);
var inst_31094__$1 = calc_state.call(null);
var inst_31096 = (inst_31094__$1 == null);
var inst_31097 = cljs.core.not.call(null,inst_31096);
var state_31190__$1 = (function (){var statearr_31195 = state_31190;
(statearr_31195[(8)] = inst_31094__$1);

return statearr_31195;
})();
if(inst_31097){
var statearr_31196_31257 = state_31190__$1;
(statearr_31196_31257[(1)] = (2));

} else {
var statearr_31197_31258 = state_31190__$1;
(statearr_31197_31258[(1)] = (3));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (24))){
var inst_31164 = (state_31190[(9)]);
var inst_31150 = (state_31190[(10)]);
var inst_31141 = (state_31190[(11)]);
var inst_31164__$1 = inst_31141.call(null,inst_31150);
var state_31190__$1 = (function (){var statearr_31198 = state_31190;
(statearr_31198[(9)] = inst_31164__$1);

return statearr_31198;
})();
if(cljs.core.truth_(inst_31164__$1)){
var statearr_31199_31259 = state_31190__$1;
(statearr_31199_31259[(1)] = (29));

} else {
var statearr_31200_31260 = state_31190__$1;
(statearr_31200_31260[(1)] = (30));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (4))){
var inst_31109 = (state_31190[(2)]);
var state_31190__$1 = state_31190;
if(cljs.core.truth_(inst_31109)){
var statearr_31201_31261 = state_31190__$1;
(statearr_31201_31261[(1)] = (8));

} else {
var statearr_31202_31262 = state_31190__$1;
(statearr_31202_31262[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (15))){
var inst_31135 = (state_31190[(2)]);
var state_31190__$1 = state_31190;
if(cljs.core.truth_(inst_31135)){
var statearr_31203_31263 = state_31190__$1;
(statearr_31203_31263[(1)] = (19));

} else {
var statearr_31204_31264 = state_31190__$1;
(statearr_31204_31264[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (21))){
var inst_31140 = (state_31190[(12)]);
var inst_31140__$1 = (state_31190[(2)]);
var inst_31141 = cljs.core.get.call(null,inst_31140__$1,new cljs.core.Keyword(null,"solos","solos",1441458643));
var inst_31142 = cljs.core.get.call(null,inst_31140__$1,new cljs.core.Keyword(null,"mutes","mutes",1068806309));
var inst_31143 = cljs.core.get.call(null,inst_31140__$1,new cljs.core.Keyword(null,"reads","reads",-1215067361));
var state_31190__$1 = (function (){var statearr_31205 = state_31190;
(statearr_31205[(12)] = inst_31140__$1);

(statearr_31205[(11)] = inst_31141);

(statearr_31205[(13)] = inst_31142);

return statearr_31205;
})();
return cljs.core.async.ioc_alts_BANG_.call(null,state_31190__$1,(22),inst_31143);
} else {
if((state_val_31191 === (31))){
var inst_31172 = (state_31190[(2)]);
var state_31190__$1 = state_31190;
if(cljs.core.truth_(inst_31172)){
var statearr_31206_31265 = state_31190__$1;
(statearr_31206_31265[(1)] = (32));

} else {
var statearr_31207_31266 = state_31190__$1;
(statearr_31207_31266[(1)] = (33));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (32))){
var inst_31149 = (state_31190[(14)]);
var state_31190__$1 = state_31190;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_31190__$1,(35),out,inst_31149);
} else {
if((state_val_31191 === (33))){
var inst_31140 = (state_31190[(12)]);
var inst_31118 = inst_31140;
var state_31190__$1 = (function (){var statearr_31208 = state_31190;
(statearr_31208[(7)] = inst_31118);

return statearr_31208;
})();
var statearr_31209_31267 = state_31190__$1;
(statearr_31209_31267[(2)] = null);

(statearr_31209_31267[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (13))){
var inst_31118 = (state_31190[(7)]);
var inst_31125 = inst_31118.cljs$lang$protocol_mask$partition0$;
var inst_31126 = (inst_31125 & (64));
var inst_31127 = inst_31118.cljs$core$ISeq$;
var inst_31128 = (inst_31126) || (inst_31127);
var state_31190__$1 = state_31190;
if(cljs.core.truth_(inst_31128)){
var statearr_31210_31268 = state_31190__$1;
(statearr_31210_31268[(1)] = (16));

} else {
var statearr_31211_31269 = state_31190__$1;
(statearr_31211_31269[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (22))){
var inst_31149 = (state_31190[(14)]);
var inst_31150 = (state_31190[(10)]);
var inst_31148 = (state_31190[(2)]);
var inst_31149__$1 = cljs.core.nth.call(null,inst_31148,(0),null);
var inst_31150__$1 = cljs.core.nth.call(null,inst_31148,(1),null);
var inst_31151 = (inst_31149__$1 == null);
var inst_31152 = cljs.core._EQ_.call(null,inst_31150__$1,change);
var inst_31153 = (inst_31151) || (inst_31152);
var state_31190__$1 = (function (){var statearr_31212 = state_31190;
(statearr_31212[(14)] = inst_31149__$1);

(statearr_31212[(10)] = inst_31150__$1);

return statearr_31212;
})();
if(cljs.core.truth_(inst_31153)){
var statearr_31213_31270 = state_31190__$1;
(statearr_31213_31270[(1)] = (23));

} else {
var statearr_31214_31271 = state_31190__$1;
(statearr_31214_31271[(1)] = (24));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (36))){
var inst_31140 = (state_31190[(12)]);
var inst_31118 = inst_31140;
var state_31190__$1 = (function (){var statearr_31215 = state_31190;
(statearr_31215[(7)] = inst_31118);

return statearr_31215;
})();
var statearr_31216_31272 = state_31190__$1;
(statearr_31216_31272[(2)] = null);

(statearr_31216_31272[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (29))){
var inst_31164 = (state_31190[(9)]);
var state_31190__$1 = state_31190;
var statearr_31217_31273 = state_31190__$1;
(statearr_31217_31273[(2)] = inst_31164);

(statearr_31217_31273[(1)] = (31));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (6))){
var state_31190__$1 = state_31190;
var statearr_31218_31274 = state_31190__$1;
(statearr_31218_31274[(2)] = false);

(statearr_31218_31274[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (28))){
var inst_31160 = (state_31190[(2)]);
var inst_31161 = calc_state.call(null);
var inst_31118 = inst_31161;
var state_31190__$1 = (function (){var statearr_31219 = state_31190;
(statearr_31219[(7)] = inst_31118);

(statearr_31219[(15)] = inst_31160);

return statearr_31219;
})();
var statearr_31220_31275 = state_31190__$1;
(statearr_31220_31275[(2)] = null);

(statearr_31220_31275[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (25))){
var inst_31186 = (state_31190[(2)]);
var state_31190__$1 = state_31190;
var statearr_31221_31276 = state_31190__$1;
(statearr_31221_31276[(2)] = inst_31186);

(statearr_31221_31276[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (34))){
var inst_31184 = (state_31190[(2)]);
var state_31190__$1 = state_31190;
var statearr_31222_31277 = state_31190__$1;
(statearr_31222_31277[(2)] = inst_31184);

(statearr_31222_31277[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (17))){
var state_31190__$1 = state_31190;
var statearr_31223_31278 = state_31190__$1;
(statearr_31223_31278[(2)] = false);

(statearr_31223_31278[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (3))){
var state_31190__$1 = state_31190;
var statearr_31224_31279 = state_31190__$1;
(statearr_31224_31279[(2)] = false);

(statearr_31224_31279[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (12))){
var inst_31188 = (state_31190[(2)]);
var state_31190__$1 = state_31190;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_31190__$1,inst_31188);
} else {
if((state_val_31191 === (2))){
var inst_31094 = (state_31190[(8)]);
var inst_31099 = inst_31094.cljs$lang$protocol_mask$partition0$;
var inst_31100 = (inst_31099 & (64));
var inst_31101 = inst_31094.cljs$core$ISeq$;
var inst_31102 = (inst_31100) || (inst_31101);
var state_31190__$1 = state_31190;
if(cljs.core.truth_(inst_31102)){
var statearr_31225_31280 = state_31190__$1;
(statearr_31225_31280[(1)] = (5));

} else {
var statearr_31226_31281 = state_31190__$1;
(statearr_31226_31281[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (23))){
var inst_31149 = (state_31190[(14)]);
var inst_31155 = (inst_31149 == null);
var state_31190__$1 = state_31190;
if(cljs.core.truth_(inst_31155)){
var statearr_31227_31282 = state_31190__$1;
(statearr_31227_31282[(1)] = (26));

} else {
var statearr_31228_31283 = state_31190__$1;
(statearr_31228_31283[(1)] = (27));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (35))){
var inst_31175 = (state_31190[(2)]);
var state_31190__$1 = state_31190;
if(cljs.core.truth_(inst_31175)){
var statearr_31229_31284 = state_31190__$1;
(statearr_31229_31284[(1)] = (36));

} else {
var statearr_31230_31285 = state_31190__$1;
(statearr_31230_31285[(1)] = (37));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (19))){
var inst_31118 = (state_31190[(7)]);
var inst_31137 = cljs.core.apply.call(null,cljs.core.hash_map,inst_31118);
var state_31190__$1 = state_31190;
var statearr_31231_31286 = state_31190__$1;
(statearr_31231_31286[(2)] = inst_31137);

(statearr_31231_31286[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (11))){
var inst_31118 = (state_31190[(7)]);
var inst_31122 = (inst_31118 == null);
var inst_31123 = cljs.core.not.call(null,inst_31122);
var state_31190__$1 = state_31190;
if(inst_31123){
var statearr_31232_31287 = state_31190__$1;
(statearr_31232_31287[(1)] = (13));

} else {
var statearr_31233_31288 = state_31190__$1;
(statearr_31233_31288[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (9))){
var inst_31094 = (state_31190[(8)]);
var state_31190__$1 = state_31190;
var statearr_31234_31289 = state_31190__$1;
(statearr_31234_31289[(2)] = inst_31094);

(statearr_31234_31289[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (5))){
var state_31190__$1 = state_31190;
var statearr_31235_31290 = state_31190__$1;
(statearr_31235_31290[(2)] = true);

(statearr_31235_31290[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (14))){
var state_31190__$1 = state_31190;
var statearr_31236_31291 = state_31190__$1;
(statearr_31236_31291[(2)] = false);

(statearr_31236_31291[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (26))){
var inst_31150 = (state_31190[(10)]);
var inst_31157 = cljs.core.swap_BANG_.call(null,cs,cljs.core.dissoc,inst_31150);
var state_31190__$1 = state_31190;
var statearr_31237_31292 = state_31190__$1;
(statearr_31237_31292[(2)] = inst_31157);

(statearr_31237_31292[(1)] = (28));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (16))){
var state_31190__$1 = state_31190;
var statearr_31238_31293 = state_31190__$1;
(statearr_31238_31293[(2)] = true);

(statearr_31238_31293[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (38))){
var inst_31180 = (state_31190[(2)]);
var state_31190__$1 = state_31190;
var statearr_31239_31294 = state_31190__$1;
(statearr_31239_31294[(2)] = inst_31180);

(statearr_31239_31294[(1)] = (34));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (30))){
var inst_31150 = (state_31190[(10)]);
var inst_31141 = (state_31190[(11)]);
var inst_31142 = (state_31190[(13)]);
var inst_31167 = cljs.core.empty_QMARK_.call(null,inst_31141);
var inst_31168 = inst_31142.call(null,inst_31150);
var inst_31169 = cljs.core.not.call(null,inst_31168);
var inst_31170 = (inst_31167) && (inst_31169);
var state_31190__$1 = state_31190;
var statearr_31240_31295 = state_31190__$1;
(statearr_31240_31295[(2)] = inst_31170);

(statearr_31240_31295[(1)] = (31));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (10))){
var inst_31094 = (state_31190[(8)]);
var inst_31114 = (state_31190[(2)]);
var inst_31115 = cljs.core.get.call(null,inst_31114,new cljs.core.Keyword(null,"solos","solos",1441458643));
var inst_31116 = cljs.core.get.call(null,inst_31114,new cljs.core.Keyword(null,"mutes","mutes",1068806309));
var inst_31117 = cljs.core.get.call(null,inst_31114,new cljs.core.Keyword(null,"reads","reads",-1215067361));
var inst_31118 = inst_31094;
var state_31190__$1 = (function (){var statearr_31241 = state_31190;
(statearr_31241[(16)] = inst_31115);

(statearr_31241[(7)] = inst_31118);

(statearr_31241[(17)] = inst_31116);

(statearr_31241[(18)] = inst_31117);

return statearr_31241;
})();
var statearr_31242_31296 = state_31190__$1;
(statearr_31242_31296[(2)] = null);

(statearr_31242_31296[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (18))){
var inst_31132 = (state_31190[(2)]);
var state_31190__$1 = state_31190;
var statearr_31243_31297 = state_31190__$1;
(statearr_31243_31297[(2)] = inst_31132);

(statearr_31243_31297[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (37))){
var state_31190__$1 = state_31190;
var statearr_31244_31298 = state_31190__$1;
(statearr_31244_31298[(2)] = null);

(statearr_31244_31298[(1)] = (38));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31191 === (8))){
var inst_31094 = (state_31190[(8)]);
var inst_31111 = cljs.core.apply.call(null,cljs.core.hash_map,inst_31094);
var state_31190__$1 = state_31190;
var statearr_31245_31299 = state_31190__$1;
(statearr_31245_31299[(2)] = inst_31111);

(statearr_31245_31299[(1)] = (10));


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
});})(c__29520__auto___31253,cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state,m))
;
return ((function (switch__29408__auto__,c__29520__auto___31253,cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state,m){
return (function() {
var cljs$core$async$mix_$_state_machine__29409__auto__ = null;
var cljs$core$async$mix_$_state_machine__29409__auto____0 = (function (){
var statearr_31249 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_31249[(0)] = cljs$core$async$mix_$_state_machine__29409__auto__);

(statearr_31249[(1)] = (1));

return statearr_31249;
});
var cljs$core$async$mix_$_state_machine__29409__auto____1 = (function (state_31190){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_31190);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e31250){if((e31250 instanceof Object)){
var ex__29412__auto__ = e31250;
var statearr_31251_31300 = state_31190;
(statearr_31251_31300[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_31190);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e31250;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__31301 = state_31190;
state_31190 = G__31301;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$mix_$_state_machine__29409__auto__ = function(state_31190){
switch(arguments.length){
case 0:
return cljs$core$async$mix_$_state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$mix_$_state_machine__29409__auto____1.call(this,state_31190);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$mix_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$mix_$_state_machine__29409__auto____0;
cljs$core$async$mix_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$mix_$_state_machine__29409__auto____1;
return cljs$core$async$mix_$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___31253,cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state,m))
})();
var state__29522__auto__ = (function (){var statearr_31252 = f__29521__auto__.call(null);
(statearr_31252[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___31253);

return statearr_31252;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___31253,cs,solo_modes,attrs,solo_mode,change,changed,pick,calc_state,m))
);


return m;
});
/**
 * Adds ch as an input to the mix
 */
cljs.core.async.admix = (function cljs$core$async$admix(mix,ch){
return cljs.core.async.admix_STAR_.call(null,mix,ch);
});
/**
 * Removes ch as an input to the mix
 */
cljs.core.async.unmix = (function cljs$core$async$unmix(mix,ch){
return cljs.core.async.unmix_STAR_.call(null,mix,ch);
});
/**
 * removes all inputs from the mix
 */
cljs.core.async.unmix_all = (function cljs$core$async$unmix_all(mix){
return cljs.core.async.unmix_all_STAR_.call(null,mix);
});
/**
 * Atomically sets the state(s) of one or more channels in a mix. The
 *   state map is a map of channels -> channel-state-map. A
 *   channel-state-map is a map of attrs -> boolean, where attr is one or
 *   more of :mute, :pause or :solo. Any states supplied are merged with
 *   the current state.
 * 
 *   Note that channels can be added to a mix via toggle, which can be
 *   used to add channels in a particular (e.g. paused) state.
 */
cljs.core.async.toggle = (function cljs$core$async$toggle(mix,state_map){
return cljs.core.async.toggle_STAR_.call(null,mix,state_map);
});
/**
 * Sets the solo mode of the mix. mode must be one of :mute or :pause
 */
cljs.core.async.solo_mode = (function cljs$core$async$solo_mode(mix,mode){
return cljs.core.async.solo_mode_STAR_.call(null,mix,mode);
});

/**
 * @interface
 */
cljs.core.async.Pub = function(){};

cljs.core.async.sub_STAR_ = (function cljs$core$async$sub_STAR_(p,v,ch,close_QMARK_){
if((!((p == null))) && (!((p.cljs$core$async$Pub$sub_STAR_$arity$4 == null)))){
return p.cljs$core$async$Pub$sub_STAR_$arity$4(p,v,ch,close_QMARK_);
} else {
var x__25453__auto__ = (((p == null))?null:p);
var m__25454__auto__ = (cljs.core.async.sub_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,p,v,ch,close_QMARK_);
} else {
var m__25454__auto____$1 = (cljs.core.async.sub_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,p,v,ch,close_QMARK_);
} else {
throw cljs.core.missing_protocol.call(null,"Pub.sub*",p);
}
}
}
});

cljs.core.async.unsub_STAR_ = (function cljs$core$async$unsub_STAR_(p,v,ch){
if((!((p == null))) && (!((p.cljs$core$async$Pub$unsub_STAR_$arity$3 == null)))){
return p.cljs$core$async$Pub$unsub_STAR_$arity$3(p,v,ch);
} else {
var x__25453__auto__ = (((p == null))?null:p);
var m__25454__auto__ = (cljs.core.async.unsub_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,p,v,ch);
} else {
var m__25454__auto____$1 = (cljs.core.async.unsub_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,p,v,ch);
} else {
throw cljs.core.missing_protocol.call(null,"Pub.unsub*",p);
}
}
}
});

cljs.core.async.unsub_all_STAR_ = (function cljs$core$async$unsub_all_STAR_(var_args){
var args31302 = [];
var len__25865__auto___31305 = arguments.length;
var i__25866__auto___31306 = (0);
while(true){
if((i__25866__auto___31306 < len__25865__auto___31305)){
args31302.push((arguments[i__25866__auto___31306]));

var G__31307 = (i__25866__auto___31306 + (1));
i__25866__auto___31306 = G__31307;
continue;
} else {
}
break;
}

var G__31304 = args31302.length;
switch (G__31304) {
case 1:
return cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args31302.length)].join('')));

}
});

cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$1 = (function (p){
if((!((p == null))) && (!((p.cljs$core$async$Pub$unsub_all_STAR_$arity$1 == null)))){
return p.cljs$core$async$Pub$unsub_all_STAR_$arity$1(p);
} else {
var x__25453__auto__ = (((p == null))?null:p);
var m__25454__auto__ = (cljs.core.async.unsub_all_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,p);
} else {
var m__25454__auto____$1 = (cljs.core.async.unsub_all_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,p);
} else {
throw cljs.core.missing_protocol.call(null,"Pub.unsub-all*",p);
}
}
}
});

cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$2 = (function (p,v){
if((!((p == null))) && (!((p.cljs$core$async$Pub$unsub_all_STAR_$arity$2 == null)))){
return p.cljs$core$async$Pub$unsub_all_STAR_$arity$2(p,v);
} else {
var x__25453__auto__ = (((p == null))?null:p);
var m__25454__auto__ = (cljs.core.async.unsub_all_STAR_[goog.typeOf(x__25453__auto__)]);
if(!((m__25454__auto__ == null))){
return m__25454__auto__.call(null,p,v);
} else {
var m__25454__auto____$1 = (cljs.core.async.unsub_all_STAR_["_"]);
if(!((m__25454__auto____$1 == null))){
return m__25454__auto____$1.call(null,p,v);
} else {
throw cljs.core.missing_protocol.call(null,"Pub.unsub-all*",p);
}
}
}
});

cljs.core.async.unsub_all_STAR_.cljs$lang$maxFixedArity = 2;


/**
 * Creates and returns a pub(lication) of the supplied channel,
 *   partitioned into topics by the topic-fn. topic-fn will be applied to
 *   each value on the channel and the result will determine the 'topic'
 *   on which that value will be put. Channels can be subscribed to
 *   receive copies of topics using 'sub', and unsubscribed using
 *   'unsub'. Each topic will be handled by an internal mult on a
 *   dedicated channel. By default these internal channels are
 *   unbuffered, but a buf-fn can be supplied which, given a topic,
 *   creates a buffer with desired properties.
 * 
 *   Each item is distributed to all subs in parallel and synchronously,
 *   i.e. each sub must accept before the next item is distributed. Use
 *   buffering/windowing to prevent slow subs from holding up the pub.
 * 
 *   Items received when there are no matching subs get dropped.
 * 
 *   Note that if buf-fns are used then each topic is handled
 *   asynchronously, i.e. if a channel is subscribed to more than one
 *   topic it should not expect them to be interleaved identically with
 *   the source.
 */
cljs.core.async.pub = (function cljs$core$async$pub(var_args){
var args31310 = [];
var len__25865__auto___31435 = arguments.length;
var i__25866__auto___31436 = (0);
while(true){
if((i__25866__auto___31436 < len__25865__auto___31435)){
args31310.push((arguments[i__25866__auto___31436]));

var G__31437 = (i__25866__auto___31436 + (1));
i__25866__auto___31436 = G__31437;
continue;
} else {
}
break;
}

var G__31312 = args31310.length;
switch (G__31312) {
case 2:
return cljs.core.async.pub.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.pub.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args31310.length)].join('')));

}
});

cljs.core.async.pub.cljs$core$IFn$_invoke$arity$2 = (function (ch,topic_fn){
return cljs.core.async.pub.call(null,ch,topic_fn,cljs.core.constantly.call(null,null));
});

cljs.core.async.pub.cljs$core$IFn$_invoke$arity$3 = (function (ch,topic_fn,buf_fn){
var mults = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var ensure_mult = ((function (mults){
return (function (topic){
var or__24790__auto__ = cljs.core.get.call(null,cljs.core.deref.call(null,mults),topic);
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
return cljs.core.get.call(null,cljs.core.swap_BANG_.call(null,mults,((function (or__24790__auto__,mults){
return (function (p1__31309_SHARP_){
if(cljs.core.truth_(p1__31309_SHARP_.call(null,topic))){
return p1__31309_SHARP_;
} else {
return cljs.core.assoc.call(null,p1__31309_SHARP_,topic,cljs.core.async.mult.call(null,cljs.core.async.chan.call(null,buf_fn.call(null,topic))));
}
});})(or__24790__auto__,mults))
),topic);
}
});})(mults))
;
var p = (function (){
if(typeof cljs.core.async.t_cljs$core$async31313 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {cljs.core.async.Pub}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.async.Mux}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async31313 = (function (ch,topic_fn,buf_fn,mults,ensure_mult,meta31314){
this.ch = ch;
this.topic_fn = topic_fn;
this.buf_fn = buf_fn;
this.mults = mults;
this.ensure_mult = ensure_mult;
this.meta31314 = meta31314;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
cljs.core.async.t_cljs$core$async31313.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (mults,ensure_mult){
return (function (_31315,meta31314__$1){
var self__ = this;
var _31315__$1 = this;
return (new cljs.core.async.t_cljs$core$async31313(self__.ch,self__.topic_fn,self__.buf_fn,self__.mults,self__.ensure_mult,meta31314__$1));
});})(mults,ensure_mult))
;

cljs.core.async.t_cljs$core$async31313.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (mults,ensure_mult){
return (function (_31315){
var self__ = this;
var _31315__$1 = this;
return self__.meta31314;
});})(mults,ensure_mult))
;

cljs.core.async.t_cljs$core$async31313.prototype.cljs$core$async$Mux$ = true;

cljs.core.async.t_cljs$core$async31313.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = ((function (mults,ensure_mult){
return (function (_){
var self__ = this;
var ___$1 = this;
return self__.ch;
});})(mults,ensure_mult))
;

cljs.core.async.t_cljs$core$async31313.prototype.cljs$core$async$Pub$ = true;

cljs.core.async.t_cljs$core$async31313.prototype.cljs$core$async$Pub$sub_STAR_$arity$4 = ((function (mults,ensure_mult){
return (function (p,topic,ch__$1,close_QMARK_){
var self__ = this;
var p__$1 = this;
var m = self__.ensure_mult.call(null,topic);
return cljs.core.async.tap.call(null,m,ch__$1,close_QMARK_);
});})(mults,ensure_mult))
;

cljs.core.async.t_cljs$core$async31313.prototype.cljs$core$async$Pub$unsub_STAR_$arity$3 = ((function (mults,ensure_mult){
return (function (p,topic,ch__$1){
var self__ = this;
var p__$1 = this;
var temp__4657__auto__ = cljs.core.get.call(null,cljs.core.deref.call(null,self__.mults),topic);
if(cljs.core.truth_(temp__4657__auto__)){
var m = temp__4657__auto__;
return cljs.core.async.untap.call(null,m,ch__$1);
} else {
return null;
}
});})(mults,ensure_mult))
;

cljs.core.async.t_cljs$core$async31313.prototype.cljs$core$async$Pub$unsub_all_STAR_$arity$1 = ((function (mults,ensure_mult){
return (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.reset_BANG_.call(null,self__.mults,cljs.core.PersistentArrayMap.EMPTY);
});})(mults,ensure_mult))
;

cljs.core.async.t_cljs$core$async31313.prototype.cljs$core$async$Pub$unsub_all_STAR_$arity$2 = ((function (mults,ensure_mult){
return (function (_,topic){
var self__ = this;
var ___$1 = this;
return cljs.core.swap_BANG_.call(null,self__.mults,cljs.core.dissoc,topic);
});})(mults,ensure_mult))
;

cljs.core.async.t_cljs$core$async31313.getBasis = ((function (mults,ensure_mult){
return (function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"topic-fn","topic-fn",-862449736,null),new cljs.core.Symbol(null,"buf-fn","buf-fn",-1200281591,null),new cljs.core.Symbol(null,"mults","mults",-461114485,null),new cljs.core.Symbol(null,"ensure-mult","ensure-mult",1796584816,null),new cljs.core.Symbol(null,"meta31314","meta31314",1548835692,null)], null);
});})(mults,ensure_mult))
;

cljs.core.async.t_cljs$core$async31313.cljs$lang$type = true;

cljs.core.async.t_cljs$core$async31313.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async31313";

cljs.core.async.t_cljs$core$async31313.cljs$lang$ctorPrWriter = ((function (mults,ensure_mult){
return (function (this__25396__auto__,writer__25397__auto__,opt__25398__auto__){
return cljs.core._write.call(null,writer__25397__auto__,"cljs.core.async/t_cljs$core$async31313");
});})(mults,ensure_mult))
;

cljs.core.async.__GT_t_cljs$core$async31313 = ((function (mults,ensure_mult){
return (function cljs$core$async$__GT_t_cljs$core$async31313(ch__$1,topic_fn__$1,buf_fn__$1,mults__$1,ensure_mult__$1,meta31314){
return (new cljs.core.async.t_cljs$core$async31313(ch__$1,topic_fn__$1,buf_fn__$1,mults__$1,ensure_mult__$1,meta31314));
});})(mults,ensure_mult))
;

}

return (new cljs.core.async.t_cljs$core$async31313(ch,topic_fn,buf_fn,mults,ensure_mult,cljs.core.PersistentArrayMap.EMPTY));
})()
;
var c__29520__auto___31439 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___31439,mults,ensure_mult,p){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___31439,mults,ensure_mult,p){
return (function (state_31387){
var state_val_31388 = (state_31387[(1)]);
if((state_val_31388 === (7))){
var inst_31383 = (state_31387[(2)]);
var state_31387__$1 = state_31387;
var statearr_31389_31440 = state_31387__$1;
(statearr_31389_31440[(2)] = inst_31383);

(statearr_31389_31440[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (20))){
var state_31387__$1 = state_31387;
var statearr_31390_31441 = state_31387__$1;
(statearr_31390_31441[(2)] = null);

(statearr_31390_31441[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (1))){
var state_31387__$1 = state_31387;
var statearr_31391_31442 = state_31387__$1;
(statearr_31391_31442[(2)] = null);

(statearr_31391_31442[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (24))){
var inst_31366 = (state_31387[(7)]);
var inst_31375 = cljs.core.swap_BANG_.call(null,mults,cljs.core.dissoc,inst_31366);
var state_31387__$1 = state_31387;
var statearr_31392_31443 = state_31387__$1;
(statearr_31392_31443[(2)] = inst_31375);

(statearr_31392_31443[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (4))){
var inst_31318 = (state_31387[(8)]);
var inst_31318__$1 = (state_31387[(2)]);
var inst_31319 = (inst_31318__$1 == null);
var state_31387__$1 = (function (){var statearr_31393 = state_31387;
(statearr_31393[(8)] = inst_31318__$1);

return statearr_31393;
})();
if(cljs.core.truth_(inst_31319)){
var statearr_31394_31444 = state_31387__$1;
(statearr_31394_31444[(1)] = (5));

} else {
var statearr_31395_31445 = state_31387__$1;
(statearr_31395_31445[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (15))){
var inst_31360 = (state_31387[(2)]);
var state_31387__$1 = state_31387;
var statearr_31396_31446 = state_31387__$1;
(statearr_31396_31446[(2)] = inst_31360);

(statearr_31396_31446[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (21))){
var inst_31380 = (state_31387[(2)]);
var state_31387__$1 = (function (){var statearr_31397 = state_31387;
(statearr_31397[(9)] = inst_31380);

return statearr_31397;
})();
var statearr_31398_31447 = state_31387__$1;
(statearr_31398_31447[(2)] = null);

(statearr_31398_31447[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (13))){
var inst_31342 = (state_31387[(10)]);
var inst_31344 = cljs.core.chunked_seq_QMARK_.call(null,inst_31342);
var state_31387__$1 = state_31387;
if(inst_31344){
var statearr_31399_31448 = state_31387__$1;
(statearr_31399_31448[(1)] = (16));

} else {
var statearr_31400_31449 = state_31387__$1;
(statearr_31400_31449[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (22))){
var inst_31372 = (state_31387[(2)]);
var state_31387__$1 = state_31387;
if(cljs.core.truth_(inst_31372)){
var statearr_31401_31450 = state_31387__$1;
(statearr_31401_31450[(1)] = (23));

} else {
var statearr_31402_31451 = state_31387__$1;
(statearr_31402_31451[(1)] = (24));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (6))){
var inst_31318 = (state_31387[(8)]);
var inst_31366 = (state_31387[(7)]);
var inst_31368 = (state_31387[(11)]);
var inst_31366__$1 = topic_fn.call(null,inst_31318);
var inst_31367 = cljs.core.deref.call(null,mults);
var inst_31368__$1 = cljs.core.get.call(null,inst_31367,inst_31366__$1);
var state_31387__$1 = (function (){var statearr_31403 = state_31387;
(statearr_31403[(7)] = inst_31366__$1);

(statearr_31403[(11)] = inst_31368__$1);

return statearr_31403;
})();
if(cljs.core.truth_(inst_31368__$1)){
var statearr_31404_31452 = state_31387__$1;
(statearr_31404_31452[(1)] = (19));

} else {
var statearr_31405_31453 = state_31387__$1;
(statearr_31405_31453[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (25))){
var inst_31377 = (state_31387[(2)]);
var state_31387__$1 = state_31387;
var statearr_31406_31454 = state_31387__$1;
(statearr_31406_31454[(2)] = inst_31377);

(statearr_31406_31454[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (17))){
var inst_31342 = (state_31387[(10)]);
var inst_31351 = cljs.core.first.call(null,inst_31342);
var inst_31352 = cljs.core.async.muxch_STAR_.call(null,inst_31351);
var inst_31353 = cljs.core.async.close_BANG_.call(null,inst_31352);
var inst_31354 = cljs.core.next.call(null,inst_31342);
var inst_31328 = inst_31354;
var inst_31329 = null;
var inst_31330 = (0);
var inst_31331 = (0);
var state_31387__$1 = (function (){var statearr_31407 = state_31387;
(statearr_31407[(12)] = inst_31330);

(statearr_31407[(13)] = inst_31353);

(statearr_31407[(14)] = inst_31328);

(statearr_31407[(15)] = inst_31331);

(statearr_31407[(16)] = inst_31329);

return statearr_31407;
})();
var statearr_31408_31455 = state_31387__$1;
(statearr_31408_31455[(2)] = null);

(statearr_31408_31455[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (3))){
var inst_31385 = (state_31387[(2)]);
var state_31387__$1 = state_31387;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_31387__$1,inst_31385);
} else {
if((state_val_31388 === (12))){
var inst_31362 = (state_31387[(2)]);
var state_31387__$1 = state_31387;
var statearr_31409_31456 = state_31387__$1;
(statearr_31409_31456[(2)] = inst_31362);

(statearr_31409_31456[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (2))){
var state_31387__$1 = state_31387;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_31387__$1,(4),ch);
} else {
if((state_val_31388 === (23))){
var state_31387__$1 = state_31387;
var statearr_31410_31457 = state_31387__$1;
(statearr_31410_31457[(2)] = null);

(statearr_31410_31457[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (19))){
var inst_31318 = (state_31387[(8)]);
var inst_31368 = (state_31387[(11)]);
var inst_31370 = cljs.core.async.muxch_STAR_.call(null,inst_31368);
var state_31387__$1 = state_31387;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_31387__$1,(22),inst_31370,inst_31318);
} else {
if((state_val_31388 === (11))){
var inst_31328 = (state_31387[(14)]);
var inst_31342 = (state_31387[(10)]);
var inst_31342__$1 = cljs.core.seq.call(null,inst_31328);
var state_31387__$1 = (function (){var statearr_31411 = state_31387;
(statearr_31411[(10)] = inst_31342__$1);

return statearr_31411;
})();
if(inst_31342__$1){
var statearr_31412_31458 = state_31387__$1;
(statearr_31412_31458[(1)] = (13));

} else {
var statearr_31413_31459 = state_31387__$1;
(statearr_31413_31459[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (9))){
var inst_31364 = (state_31387[(2)]);
var state_31387__$1 = state_31387;
var statearr_31414_31460 = state_31387__$1;
(statearr_31414_31460[(2)] = inst_31364);

(statearr_31414_31460[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (5))){
var inst_31325 = cljs.core.deref.call(null,mults);
var inst_31326 = cljs.core.vals.call(null,inst_31325);
var inst_31327 = cljs.core.seq.call(null,inst_31326);
var inst_31328 = inst_31327;
var inst_31329 = null;
var inst_31330 = (0);
var inst_31331 = (0);
var state_31387__$1 = (function (){var statearr_31415 = state_31387;
(statearr_31415[(12)] = inst_31330);

(statearr_31415[(14)] = inst_31328);

(statearr_31415[(15)] = inst_31331);

(statearr_31415[(16)] = inst_31329);

return statearr_31415;
})();
var statearr_31416_31461 = state_31387__$1;
(statearr_31416_31461[(2)] = null);

(statearr_31416_31461[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (14))){
var state_31387__$1 = state_31387;
var statearr_31420_31462 = state_31387__$1;
(statearr_31420_31462[(2)] = null);

(statearr_31420_31462[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (16))){
var inst_31342 = (state_31387[(10)]);
var inst_31346 = cljs.core.chunk_first.call(null,inst_31342);
var inst_31347 = cljs.core.chunk_rest.call(null,inst_31342);
var inst_31348 = cljs.core.count.call(null,inst_31346);
var inst_31328 = inst_31347;
var inst_31329 = inst_31346;
var inst_31330 = inst_31348;
var inst_31331 = (0);
var state_31387__$1 = (function (){var statearr_31421 = state_31387;
(statearr_31421[(12)] = inst_31330);

(statearr_31421[(14)] = inst_31328);

(statearr_31421[(15)] = inst_31331);

(statearr_31421[(16)] = inst_31329);

return statearr_31421;
})();
var statearr_31422_31463 = state_31387__$1;
(statearr_31422_31463[(2)] = null);

(statearr_31422_31463[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (10))){
var inst_31330 = (state_31387[(12)]);
var inst_31328 = (state_31387[(14)]);
var inst_31331 = (state_31387[(15)]);
var inst_31329 = (state_31387[(16)]);
var inst_31336 = cljs.core._nth.call(null,inst_31329,inst_31331);
var inst_31337 = cljs.core.async.muxch_STAR_.call(null,inst_31336);
var inst_31338 = cljs.core.async.close_BANG_.call(null,inst_31337);
var inst_31339 = (inst_31331 + (1));
var tmp31417 = inst_31330;
var tmp31418 = inst_31328;
var tmp31419 = inst_31329;
var inst_31328__$1 = tmp31418;
var inst_31329__$1 = tmp31419;
var inst_31330__$1 = tmp31417;
var inst_31331__$1 = inst_31339;
var state_31387__$1 = (function (){var statearr_31423 = state_31387;
(statearr_31423[(12)] = inst_31330__$1);

(statearr_31423[(17)] = inst_31338);

(statearr_31423[(14)] = inst_31328__$1);

(statearr_31423[(15)] = inst_31331__$1);

(statearr_31423[(16)] = inst_31329__$1);

return statearr_31423;
})();
var statearr_31424_31464 = state_31387__$1;
(statearr_31424_31464[(2)] = null);

(statearr_31424_31464[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (18))){
var inst_31357 = (state_31387[(2)]);
var state_31387__$1 = state_31387;
var statearr_31425_31465 = state_31387__$1;
(statearr_31425_31465[(2)] = inst_31357);

(statearr_31425_31465[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (8))){
var inst_31330 = (state_31387[(12)]);
var inst_31331 = (state_31387[(15)]);
var inst_31333 = (inst_31331 < inst_31330);
var inst_31334 = inst_31333;
var state_31387__$1 = state_31387;
if(cljs.core.truth_(inst_31334)){
var statearr_31426_31466 = state_31387__$1;
(statearr_31426_31466[(1)] = (10));

} else {
var statearr_31427_31467 = state_31387__$1;
(statearr_31427_31467[(1)] = (11));

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
});})(c__29520__auto___31439,mults,ensure_mult,p))
;
return ((function (switch__29408__auto__,c__29520__auto___31439,mults,ensure_mult,p){
return (function() {
var cljs$core$async$state_machine__29409__auto__ = null;
var cljs$core$async$state_machine__29409__auto____0 = (function (){
var statearr_31431 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_31431[(0)] = cljs$core$async$state_machine__29409__auto__);

(statearr_31431[(1)] = (1));

return statearr_31431;
});
var cljs$core$async$state_machine__29409__auto____1 = (function (state_31387){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_31387);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e31432){if((e31432 instanceof Object)){
var ex__29412__auto__ = e31432;
var statearr_31433_31468 = state_31387;
(statearr_31433_31468[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_31387);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e31432;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__31469 = state_31387;
state_31387 = G__31469;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$state_machine__29409__auto__ = function(state_31387){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29409__auto____1.call(this,state_31387);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29409__auto____0;
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29409__auto____1;
return cljs$core$async$state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___31439,mults,ensure_mult,p))
})();
var state__29522__auto__ = (function (){var statearr_31434 = f__29521__auto__.call(null);
(statearr_31434[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___31439);

return statearr_31434;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___31439,mults,ensure_mult,p))
);


return p;
});

cljs.core.async.pub.cljs$lang$maxFixedArity = 3;

/**
 * Subscribes a channel to a topic of a pub.
 * 
 *   By default the channel will be closed when the source closes,
 *   but can be determined by the close? parameter.
 */
cljs.core.async.sub = (function cljs$core$async$sub(var_args){
var args31470 = [];
var len__25865__auto___31473 = arguments.length;
var i__25866__auto___31474 = (0);
while(true){
if((i__25866__auto___31474 < len__25865__auto___31473)){
args31470.push((arguments[i__25866__auto___31474]));

var G__31475 = (i__25866__auto___31474 + (1));
i__25866__auto___31474 = G__31475;
continue;
} else {
}
break;
}

var G__31472 = args31470.length;
switch (G__31472) {
case 3:
return cljs.core.async.sub.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return cljs.core.async.sub.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args31470.length)].join('')));

}
});

cljs.core.async.sub.cljs$core$IFn$_invoke$arity$3 = (function (p,topic,ch){
return cljs.core.async.sub.call(null,p,topic,ch,true);
});

cljs.core.async.sub.cljs$core$IFn$_invoke$arity$4 = (function (p,topic,ch,close_QMARK_){
return cljs.core.async.sub_STAR_.call(null,p,topic,ch,close_QMARK_);
});

cljs.core.async.sub.cljs$lang$maxFixedArity = 4;

/**
 * Unsubscribes a channel from a topic of a pub
 */
cljs.core.async.unsub = (function cljs$core$async$unsub(p,topic,ch){
return cljs.core.async.unsub_STAR_.call(null,p,topic,ch);
});
/**
 * Unsubscribes all channels from a pub, or a topic of a pub
 */
cljs.core.async.unsub_all = (function cljs$core$async$unsub_all(var_args){
var args31477 = [];
var len__25865__auto___31480 = arguments.length;
var i__25866__auto___31481 = (0);
while(true){
if((i__25866__auto___31481 < len__25865__auto___31480)){
args31477.push((arguments[i__25866__auto___31481]));

var G__31482 = (i__25866__auto___31481 + (1));
i__25866__auto___31481 = G__31482;
continue;
} else {
}
break;
}

var G__31479 = args31477.length;
switch (G__31479) {
case 1:
return cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args31477.length)].join('')));

}
});

cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$1 = (function (p){
return cljs.core.async.unsub_all_STAR_.call(null,p);
});

cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$2 = (function (p,topic){
return cljs.core.async.unsub_all_STAR_.call(null,p,topic);
});

cljs.core.async.unsub_all.cljs$lang$maxFixedArity = 2;

/**
 * Takes a function and a collection of source channels, and returns a
 *   channel which contains the values produced by applying f to the set
 *   of first items taken from each source channel, followed by applying
 *   f to the set of second items from each channel, until any one of the
 *   channels is closed, at which point the output channel will be
 *   closed. The returned channel will be unbuffered by default, or a
 *   buf-or-n can be supplied
 */
cljs.core.async.map = (function cljs$core$async$map(var_args){
var args31484 = [];
var len__25865__auto___31555 = arguments.length;
var i__25866__auto___31556 = (0);
while(true){
if((i__25866__auto___31556 < len__25865__auto___31555)){
args31484.push((arguments[i__25866__auto___31556]));

var G__31557 = (i__25866__auto___31556 + (1));
i__25866__auto___31556 = G__31557;
continue;
} else {
}
break;
}

var G__31486 = args31484.length;
switch (G__31486) {
case 2:
return cljs.core.async.map.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.map.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args31484.length)].join('')));

}
});

cljs.core.async.map.cljs$core$IFn$_invoke$arity$2 = (function (f,chs){
return cljs.core.async.map.call(null,f,chs,null);
});

cljs.core.async.map.cljs$core$IFn$_invoke$arity$3 = (function (f,chs,buf_or_n){
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
var c__29520__auto___31559 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___31559,chs__$1,out,cnt,rets,dchan,dctr,done){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___31559,chs__$1,out,cnt,rets,dchan,dctr,done){
return (function (state_31525){
var state_val_31526 = (state_31525[(1)]);
if((state_val_31526 === (7))){
var state_31525__$1 = state_31525;
var statearr_31527_31560 = state_31525__$1;
(statearr_31527_31560[(2)] = null);

(statearr_31527_31560[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31526 === (1))){
var state_31525__$1 = state_31525;
var statearr_31528_31561 = state_31525__$1;
(statearr_31528_31561[(2)] = null);

(statearr_31528_31561[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31526 === (4))){
var inst_31489 = (state_31525[(7)]);
var inst_31491 = (inst_31489 < cnt);
var state_31525__$1 = state_31525;
if(cljs.core.truth_(inst_31491)){
var statearr_31529_31562 = state_31525__$1;
(statearr_31529_31562[(1)] = (6));

} else {
var statearr_31530_31563 = state_31525__$1;
(statearr_31530_31563[(1)] = (7));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31526 === (15))){
var inst_31521 = (state_31525[(2)]);
var state_31525__$1 = state_31525;
var statearr_31531_31564 = state_31525__$1;
(statearr_31531_31564[(2)] = inst_31521);

(statearr_31531_31564[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31526 === (13))){
var inst_31514 = cljs.core.async.close_BANG_.call(null,out);
var state_31525__$1 = state_31525;
var statearr_31532_31565 = state_31525__$1;
(statearr_31532_31565[(2)] = inst_31514);

(statearr_31532_31565[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31526 === (6))){
var state_31525__$1 = state_31525;
var statearr_31533_31566 = state_31525__$1;
(statearr_31533_31566[(2)] = null);

(statearr_31533_31566[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31526 === (3))){
var inst_31523 = (state_31525[(2)]);
var state_31525__$1 = state_31525;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_31525__$1,inst_31523);
} else {
if((state_val_31526 === (12))){
var inst_31511 = (state_31525[(8)]);
var inst_31511__$1 = (state_31525[(2)]);
var inst_31512 = cljs.core.some.call(null,cljs.core.nil_QMARK_,inst_31511__$1);
var state_31525__$1 = (function (){var statearr_31534 = state_31525;
(statearr_31534[(8)] = inst_31511__$1);

return statearr_31534;
})();
if(cljs.core.truth_(inst_31512)){
var statearr_31535_31567 = state_31525__$1;
(statearr_31535_31567[(1)] = (13));

} else {
var statearr_31536_31568 = state_31525__$1;
(statearr_31536_31568[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31526 === (2))){
var inst_31488 = cljs.core.reset_BANG_.call(null,dctr,cnt);
var inst_31489 = (0);
var state_31525__$1 = (function (){var statearr_31537 = state_31525;
(statearr_31537[(7)] = inst_31489);

(statearr_31537[(9)] = inst_31488);

return statearr_31537;
})();
var statearr_31538_31569 = state_31525__$1;
(statearr_31538_31569[(2)] = null);

(statearr_31538_31569[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31526 === (11))){
var inst_31489 = (state_31525[(7)]);
var _ = cljs.core.async.impl.ioc_helpers.add_exception_frame.call(null,state_31525,(10),Object,null,(9));
var inst_31498 = chs__$1.call(null,inst_31489);
var inst_31499 = done.call(null,inst_31489);
var inst_31500 = cljs.core.async.take_BANG_.call(null,inst_31498,inst_31499);
var state_31525__$1 = state_31525;
var statearr_31539_31570 = state_31525__$1;
(statearr_31539_31570[(2)] = inst_31500);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_31525__$1);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31526 === (9))){
var inst_31489 = (state_31525[(7)]);
var inst_31502 = (state_31525[(2)]);
var inst_31503 = (inst_31489 + (1));
var inst_31489__$1 = inst_31503;
var state_31525__$1 = (function (){var statearr_31540 = state_31525;
(statearr_31540[(10)] = inst_31502);

(statearr_31540[(7)] = inst_31489__$1);

return statearr_31540;
})();
var statearr_31541_31571 = state_31525__$1;
(statearr_31541_31571[(2)] = null);

(statearr_31541_31571[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31526 === (5))){
var inst_31509 = (state_31525[(2)]);
var state_31525__$1 = (function (){var statearr_31542 = state_31525;
(statearr_31542[(11)] = inst_31509);

return statearr_31542;
})();
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_31525__$1,(12),dchan);
} else {
if((state_val_31526 === (14))){
var inst_31511 = (state_31525[(8)]);
var inst_31516 = cljs.core.apply.call(null,f,inst_31511);
var state_31525__$1 = state_31525;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_31525__$1,(16),out,inst_31516);
} else {
if((state_val_31526 === (16))){
var inst_31518 = (state_31525[(2)]);
var state_31525__$1 = (function (){var statearr_31543 = state_31525;
(statearr_31543[(12)] = inst_31518);

return statearr_31543;
})();
var statearr_31544_31572 = state_31525__$1;
(statearr_31544_31572[(2)] = null);

(statearr_31544_31572[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31526 === (10))){
var inst_31493 = (state_31525[(2)]);
var inst_31494 = cljs.core.swap_BANG_.call(null,dctr,cljs.core.dec);
var state_31525__$1 = (function (){var statearr_31545 = state_31525;
(statearr_31545[(13)] = inst_31493);

return statearr_31545;
})();
var statearr_31546_31573 = state_31525__$1;
(statearr_31546_31573[(2)] = inst_31494);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_31525__$1);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31526 === (8))){
var inst_31507 = (state_31525[(2)]);
var state_31525__$1 = state_31525;
var statearr_31547_31574 = state_31525__$1;
(statearr_31547_31574[(2)] = inst_31507);

(statearr_31547_31574[(1)] = (5));


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
});})(c__29520__auto___31559,chs__$1,out,cnt,rets,dchan,dctr,done))
;
return ((function (switch__29408__auto__,c__29520__auto___31559,chs__$1,out,cnt,rets,dchan,dctr,done){
return (function() {
var cljs$core$async$state_machine__29409__auto__ = null;
var cljs$core$async$state_machine__29409__auto____0 = (function (){
var statearr_31551 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_31551[(0)] = cljs$core$async$state_machine__29409__auto__);

(statearr_31551[(1)] = (1));

return statearr_31551;
});
var cljs$core$async$state_machine__29409__auto____1 = (function (state_31525){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_31525);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e31552){if((e31552 instanceof Object)){
var ex__29412__auto__ = e31552;
var statearr_31553_31575 = state_31525;
(statearr_31553_31575[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_31525);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e31552;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__31576 = state_31525;
state_31525 = G__31576;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$state_machine__29409__auto__ = function(state_31525){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29409__auto____1.call(this,state_31525);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29409__auto____0;
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29409__auto____1;
return cljs$core$async$state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___31559,chs__$1,out,cnt,rets,dchan,dctr,done))
})();
var state__29522__auto__ = (function (){var statearr_31554 = f__29521__auto__.call(null);
(statearr_31554[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___31559);

return statearr_31554;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___31559,chs__$1,out,cnt,rets,dchan,dctr,done))
);


return out;
});

cljs.core.async.map.cljs$lang$maxFixedArity = 3;

/**
 * Takes a collection of source channels and returns a channel which
 *   contains all values taken from them. The returned channel will be
 *   unbuffered by default, or a buf-or-n can be supplied. The channel
 *   will close after all the source channels have closed.
 */
cljs.core.async.merge = (function cljs$core$async$merge(var_args){
var args31578 = [];
var len__25865__auto___31636 = arguments.length;
var i__25866__auto___31637 = (0);
while(true){
if((i__25866__auto___31637 < len__25865__auto___31636)){
args31578.push((arguments[i__25866__auto___31637]));

var G__31638 = (i__25866__auto___31637 + (1));
i__25866__auto___31637 = G__31638;
continue;
} else {
}
break;
}

var G__31580 = args31578.length;
switch (G__31580) {
case 1:
return cljs.core.async.merge.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.merge.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args31578.length)].join('')));

}
});

cljs.core.async.merge.cljs$core$IFn$_invoke$arity$1 = (function (chs){
return cljs.core.async.merge.call(null,chs,null);
});

cljs.core.async.merge.cljs$core$IFn$_invoke$arity$2 = (function (chs,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
var c__29520__auto___31640 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___31640,out){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___31640,out){
return (function (state_31612){
var state_val_31613 = (state_31612[(1)]);
if((state_val_31613 === (7))){
var inst_31591 = (state_31612[(7)]);
var inst_31592 = (state_31612[(8)]);
var inst_31591__$1 = (state_31612[(2)]);
var inst_31592__$1 = cljs.core.nth.call(null,inst_31591__$1,(0),null);
var inst_31593 = cljs.core.nth.call(null,inst_31591__$1,(1),null);
var inst_31594 = (inst_31592__$1 == null);
var state_31612__$1 = (function (){var statearr_31614 = state_31612;
(statearr_31614[(7)] = inst_31591__$1);

(statearr_31614[(9)] = inst_31593);

(statearr_31614[(8)] = inst_31592__$1);

return statearr_31614;
})();
if(cljs.core.truth_(inst_31594)){
var statearr_31615_31641 = state_31612__$1;
(statearr_31615_31641[(1)] = (8));

} else {
var statearr_31616_31642 = state_31612__$1;
(statearr_31616_31642[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31613 === (1))){
var inst_31581 = cljs.core.vec.call(null,chs);
var inst_31582 = inst_31581;
var state_31612__$1 = (function (){var statearr_31617 = state_31612;
(statearr_31617[(10)] = inst_31582);

return statearr_31617;
})();
var statearr_31618_31643 = state_31612__$1;
(statearr_31618_31643[(2)] = null);

(statearr_31618_31643[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31613 === (4))){
var inst_31582 = (state_31612[(10)]);
var state_31612__$1 = state_31612;
return cljs.core.async.ioc_alts_BANG_.call(null,state_31612__$1,(7),inst_31582);
} else {
if((state_val_31613 === (6))){
var inst_31608 = (state_31612[(2)]);
var state_31612__$1 = state_31612;
var statearr_31619_31644 = state_31612__$1;
(statearr_31619_31644[(2)] = inst_31608);

(statearr_31619_31644[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31613 === (3))){
var inst_31610 = (state_31612[(2)]);
var state_31612__$1 = state_31612;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_31612__$1,inst_31610);
} else {
if((state_val_31613 === (2))){
var inst_31582 = (state_31612[(10)]);
var inst_31584 = cljs.core.count.call(null,inst_31582);
var inst_31585 = (inst_31584 > (0));
var state_31612__$1 = state_31612;
if(cljs.core.truth_(inst_31585)){
var statearr_31621_31645 = state_31612__$1;
(statearr_31621_31645[(1)] = (4));

} else {
var statearr_31622_31646 = state_31612__$1;
(statearr_31622_31646[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31613 === (11))){
var inst_31582 = (state_31612[(10)]);
var inst_31601 = (state_31612[(2)]);
var tmp31620 = inst_31582;
var inst_31582__$1 = tmp31620;
var state_31612__$1 = (function (){var statearr_31623 = state_31612;
(statearr_31623[(10)] = inst_31582__$1);

(statearr_31623[(11)] = inst_31601);

return statearr_31623;
})();
var statearr_31624_31647 = state_31612__$1;
(statearr_31624_31647[(2)] = null);

(statearr_31624_31647[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31613 === (9))){
var inst_31592 = (state_31612[(8)]);
var state_31612__$1 = state_31612;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_31612__$1,(11),out,inst_31592);
} else {
if((state_val_31613 === (5))){
var inst_31606 = cljs.core.async.close_BANG_.call(null,out);
var state_31612__$1 = state_31612;
var statearr_31625_31648 = state_31612__$1;
(statearr_31625_31648[(2)] = inst_31606);

(statearr_31625_31648[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31613 === (10))){
var inst_31604 = (state_31612[(2)]);
var state_31612__$1 = state_31612;
var statearr_31626_31649 = state_31612__$1;
(statearr_31626_31649[(2)] = inst_31604);

(statearr_31626_31649[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31613 === (8))){
var inst_31582 = (state_31612[(10)]);
var inst_31591 = (state_31612[(7)]);
var inst_31593 = (state_31612[(9)]);
var inst_31592 = (state_31612[(8)]);
var inst_31596 = (function (){var cs = inst_31582;
var vec__31587 = inst_31591;
var v = inst_31592;
var c = inst_31593;
return ((function (cs,vec__31587,v,c,inst_31582,inst_31591,inst_31593,inst_31592,state_val_31613,c__29520__auto___31640,out){
return (function (p1__31577_SHARP_){
return cljs.core.not_EQ_.call(null,c,p1__31577_SHARP_);
});
;})(cs,vec__31587,v,c,inst_31582,inst_31591,inst_31593,inst_31592,state_val_31613,c__29520__auto___31640,out))
})();
var inst_31597 = cljs.core.filterv.call(null,inst_31596,inst_31582);
var inst_31582__$1 = inst_31597;
var state_31612__$1 = (function (){var statearr_31627 = state_31612;
(statearr_31627[(10)] = inst_31582__$1);

return statearr_31627;
})();
var statearr_31628_31650 = state_31612__$1;
(statearr_31628_31650[(2)] = null);

(statearr_31628_31650[(1)] = (2));


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
});})(c__29520__auto___31640,out))
;
return ((function (switch__29408__auto__,c__29520__auto___31640,out){
return (function() {
var cljs$core$async$state_machine__29409__auto__ = null;
var cljs$core$async$state_machine__29409__auto____0 = (function (){
var statearr_31632 = [null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_31632[(0)] = cljs$core$async$state_machine__29409__auto__);

(statearr_31632[(1)] = (1));

return statearr_31632;
});
var cljs$core$async$state_machine__29409__auto____1 = (function (state_31612){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_31612);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e31633){if((e31633 instanceof Object)){
var ex__29412__auto__ = e31633;
var statearr_31634_31651 = state_31612;
(statearr_31634_31651[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_31612);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e31633;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__31652 = state_31612;
state_31612 = G__31652;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$state_machine__29409__auto__ = function(state_31612){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29409__auto____1.call(this,state_31612);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29409__auto____0;
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29409__auto____1;
return cljs$core$async$state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___31640,out))
})();
var state__29522__auto__ = (function (){var statearr_31635 = f__29521__auto__.call(null);
(statearr_31635[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___31640);

return statearr_31635;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___31640,out))
);


return out;
});

cljs.core.async.merge.cljs$lang$maxFixedArity = 2;

/**
 * Returns a channel containing the single (collection) result of the
 *   items taken from the channel conjoined to the supplied
 *   collection. ch must close before into produces a result.
 */
cljs.core.async.into = (function cljs$core$async$into(coll,ch){
return cljs.core.async.reduce.call(null,cljs.core.conj,coll,ch);
});
/**
 * Returns a channel that will return, at most, n items from ch. After n items
 * have been returned, or ch has been closed, the return chanel will close.
 * 
 *   The output channel is unbuffered by default, unless buf-or-n is given.
 */
cljs.core.async.take = (function cljs$core$async$take(var_args){
var args31653 = [];
var len__25865__auto___31702 = arguments.length;
var i__25866__auto___31703 = (0);
while(true){
if((i__25866__auto___31703 < len__25865__auto___31702)){
args31653.push((arguments[i__25866__auto___31703]));

var G__31704 = (i__25866__auto___31703 + (1));
i__25866__auto___31703 = G__31704;
continue;
} else {
}
break;
}

var G__31655 = args31653.length;
switch (G__31655) {
case 2:
return cljs.core.async.take.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.take.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args31653.length)].join('')));

}
});

cljs.core.async.take.cljs$core$IFn$_invoke$arity$2 = (function (n,ch){
return cljs.core.async.take.call(null,n,ch,null);
});

cljs.core.async.take.cljs$core$IFn$_invoke$arity$3 = (function (n,ch,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
var c__29520__auto___31706 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___31706,out){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___31706,out){
return (function (state_31679){
var state_val_31680 = (state_31679[(1)]);
if((state_val_31680 === (7))){
var inst_31661 = (state_31679[(7)]);
var inst_31661__$1 = (state_31679[(2)]);
var inst_31662 = (inst_31661__$1 == null);
var inst_31663 = cljs.core.not.call(null,inst_31662);
var state_31679__$1 = (function (){var statearr_31681 = state_31679;
(statearr_31681[(7)] = inst_31661__$1);

return statearr_31681;
})();
if(inst_31663){
var statearr_31682_31707 = state_31679__$1;
(statearr_31682_31707[(1)] = (8));

} else {
var statearr_31683_31708 = state_31679__$1;
(statearr_31683_31708[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31680 === (1))){
var inst_31656 = (0);
var state_31679__$1 = (function (){var statearr_31684 = state_31679;
(statearr_31684[(8)] = inst_31656);

return statearr_31684;
})();
var statearr_31685_31709 = state_31679__$1;
(statearr_31685_31709[(2)] = null);

(statearr_31685_31709[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31680 === (4))){
var state_31679__$1 = state_31679;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_31679__$1,(7),ch);
} else {
if((state_val_31680 === (6))){
var inst_31674 = (state_31679[(2)]);
var state_31679__$1 = state_31679;
var statearr_31686_31710 = state_31679__$1;
(statearr_31686_31710[(2)] = inst_31674);

(statearr_31686_31710[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31680 === (3))){
var inst_31676 = (state_31679[(2)]);
var inst_31677 = cljs.core.async.close_BANG_.call(null,out);
var state_31679__$1 = (function (){var statearr_31687 = state_31679;
(statearr_31687[(9)] = inst_31676);

return statearr_31687;
})();
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_31679__$1,inst_31677);
} else {
if((state_val_31680 === (2))){
var inst_31656 = (state_31679[(8)]);
var inst_31658 = (inst_31656 < n);
var state_31679__$1 = state_31679;
if(cljs.core.truth_(inst_31658)){
var statearr_31688_31711 = state_31679__$1;
(statearr_31688_31711[(1)] = (4));

} else {
var statearr_31689_31712 = state_31679__$1;
(statearr_31689_31712[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31680 === (11))){
var inst_31656 = (state_31679[(8)]);
var inst_31666 = (state_31679[(2)]);
var inst_31667 = (inst_31656 + (1));
var inst_31656__$1 = inst_31667;
var state_31679__$1 = (function (){var statearr_31690 = state_31679;
(statearr_31690[(8)] = inst_31656__$1);

(statearr_31690[(10)] = inst_31666);

return statearr_31690;
})();
var statearr_31691_31713 = state_31679__$1;
(statearr_31691_31713[(2)] = null);

(statearr_31691_31713[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31680 === (9))){
var state_31679__$1 = state_31679;
var statearr_31692_31714 = state_31679__$1;
(statearr_31692_31714[(2)] = null);

(statearr_31692_31714[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31680 === (5))){
var state_31679__$1 = state_31679;
var statearr_31693_31715 = state_31679__$1;
(statearr_31693_31715[(2)] = null);

(statearr_31693_31715[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31680 === (10))){
var inst_31671 = (state_31679[(2)]);
var state_31679__$1 = state_31679;
var statearr_31694_31716 = state_31679__$1;
(statearr_31694_31716[(2)] = inst_31671);

(statearr_31694_31716[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31680 === (8))){
var inst_31661 = (state_31679[(7)]);
var state_31679__$1 = state_31679;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_31679__$1,(11),out,inst_31661);
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
});})(c__29520__auto___31706,out))
;
return ((function (switch__29408__auto__,c__29520__auto___31706,out){
return (function() {
var cljs$core$async$state_machine__29409__auto__ = null;
var cljs$core$async$state_machine__29409__auto____0 = (function (){
var statearr_31698 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_31698[(0)] = cljs$core$async$state_machine__29409__auto__);

(statearr_31698[(1)] = (1));

return statearr_31698;
});
var cljs$core$async$state_machine__29409__auto____1 = (function (state_31679){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_31679);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e31699){if((e31699 instanceof Object)){
var ex__29412__auto__ = e31699;
var statearr_31700_31717 = state_31679;
(statearr_31700_31717[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_31679);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e31699;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__31718 = state_31679;
state_31679 = G__31718;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$state_machine__29409__auto__ = function(state_31679){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29409__auto____1.call(this,state_31679);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29409__auto____0;
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29409__auto____1;
return cljs$core$async$state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___31706,out))
})();
var state__29522__auto__ = (function (){var statearr_31701 = f__29521__auto__.call(null);
(statearr_31701[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___31706);

return statearr_31701;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___31706,out))
);


return out;
});

cljs.core.async.take.cljs$lang$maxFixedArity = 3;

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.map_LT_ = (function cljs$core$async$map_LT_(f,ch){
if(typeof cljs.core.async.t_cljs$core$async31726 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Channel}
 * @implements {cljs.core.async.impl.protocols.WritePort}
 * @implements {cljs.core.async.impl.protocols.ReadPort}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async31726 = (function (map_LT_,f,ch,meta31727){
this.map_LT_ = map_LT_;
this.f = f;
this.ch = ch;
this.meta31727 = meta31727;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
cljs.core.async.t_cljs$core$async31726.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_31728,meta31727__$1){
var self__ = this;
var _31728__$1 = this;
return (new cljs.core.async.t_cljs$core$async31726(self__.map_LT_,self__.f,self__.ch,meta31727__$1));
});

cljs.core.async.t_cljs$core$async31726.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_31728){
var self__ = this;
var _31728__$1 = this;
return self__.meta31727;
});

cljs.core.async.t_cljs$core$async31726.prototype.cljs$core$async$impl$protocols$Channel$ = true;

cljs.core.async.t_cljs$core$async31726.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_.call(null,self__.ch);
});

cljs.core.async.t_cljs$core$async31726.prototype.cljs$core$async$impl$protocols$Channel$closed_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.closed_QMARK_.call(null,self__.ch);
});

cljs.core.async.t_cljs$core$async31726.prototype.cljs$core$async$impl$protocols$ReadPort$ = true;

cljs.core.async.t_cljs$core$async31726.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
var ret = cljs.core.async.impl.protocols.take_BANG_.call(null,self__.ch,(function (){
if(typeof cljs.core.async.t_cljs$core$async31729 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async31729 = (function (map_LT_,f,ch,meta31727,_,fn1,meta31730){
this.map_LT_ = map_LT_;
this.f = f;
this.ch = ch;
this.meta31727 = meta31727;
this._ = _;
this.fn1 = fn1;
this.meta31730 = meta31730;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
cljs.core.async.t_cljs$core$async31729.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (___$1){
return (function (_31731,meta31730__$1){
var self__ = this;
var _31731__$1 = this;
return (new cljs.core.async.t_cljs$core$async31729(self__.map_LT_,self__.f,self__.ch,self__.meta31727,self__._,self__.fn1,meta31730__$1));
});})(___$1))
;

cljs.core.async.t_cljs$core$async31729.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (___$1){
return (function (_31731){
var self__ = this;
var _31731__$1 = this;
return self__.meta31730;
});})(___$1))
;

cljs.core.async.t_cljs$core$async31729.prototype.cljs$core$async$impl$protocols$Handler$ = true;

cljs.core.async.t_cljs$core$async31729.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = ((function (___$1){
return (function (___$1){
var self__ = this;
var ___$2 = this;
return cljs.core.async.impl.protocols.active_QMARK_.call(null,self__.fn1);
});})(___$1))
;

cljs.core.async.t_cljs$core$async31729.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = ((function (___$1){
return (function (___$1){
var self__ = this;
var ___$2 = this;
return true;
});})(___$1))
;

cljs.core.async.t_cljs$core$async31729.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = ((function (___$1){
return (function (___$1){
var self__ = this;
var ___$2 = this;
var f1 = cljs.core.async.impl.protocols.commit.call(null,self__.fn1);
return ((function (f1,___$2,___$1){
return (function (p1__31719_SHARP_){
return f1.call(null,(((p1__31719_SHARP_ == null))?null:self__.f.call(null,p1__31719_SHARP_)));
});
;})(f1,___$2,___$1))
});})(___$1))
;

cljs.core.async.t_cljs$core$async31729.getBasis = ((function (___$1){
return (function (){
return new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.with_meta(new cljs.core.Symbol(null,"map<","map<",-1235808357,null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"arglists","arglists",1661989754),cljs.core.list(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.list(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null)], null))),new cljs.core.Keyword(null,"doc","doc",1913296891),"Deprecated - this function will be removed. Use transducer instead"], null)),new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta31727","meta31727",1198028729,null),cljs.core.with_meta(new cljs.core.Symbol(null,"_","_",-1201019570,null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Symbol("cljs.core.async","t_cljs$core$async31726","cljs.core.async/t_cljs$core$async31726",-475900387,null)], null)),new cljs.core.Symbol(null,"fn1","fn1",895834444,null),new cljs.core.Symbol(null,"meta31730","meta31730",-1159809662,null)], null);
});})(___$1))
;

cljs.core.async.t_cljs$core$async31729.cljs$lang$type = true;

cljs.core.async.t_cljs$core$async31729.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async31729";

cljs.core.async.t_cljs$core$async31729.cljs$lang$ctorPrWriter = ((function (___$1){
return (function (this__25396__auto__,writer__25397__auto__,opt__25398__auto__){
return cljs.core._write.call(null,writer__25397__auto__,"cljs.core.async/t_cljs$core$async31729");
});})(___$1))
;

cljs.core.async.__GT_t_cljs$core$async31729 = ((function (___$1){
return (function cljs$core$async$map_LT__$___GT_t_cljs$core$async31729(map_LT___$1,f__$1,ch__$1,meta31727__$1,___$2,fn1__$1,meta31730){
return (new cljs.core.async.t_cljs$core$async31729(map_LT___$1,f__$1,ch__$1,meta31727__$1,___$2,fn1__$1,meta31730));
});})(___$1))
;

}

return (new cljs.core.async.t_cljs$core$async31729(self__.map_LT_,self__.f,self__.ch,self__.meta31727,___$1,fn1,cljs.core.PersistentArrayMap.EMPTY));
})()
);
if(cljs.core.truth_((function (){var and__24778__auto__ = ret;
if(cljs.core.truth_(and__24778__auto__)){
return !((cljs.core.deref.call(null,ret) == null));
} else {
return and__24778__auto__;
}
})())){
return cljs.core.async.impl.channels.box.call(null,self__.f.call(null,cljs.core.deref.call(null,ret)));
} else {
return ret;
}
});

cljs.core.async.t_cljs$core$async31726.prototype.cljs$core$async$impl$protocols$WritePort$ = true;

cljs.core.async.t_cljs$core$async31726.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.put_BANG_.call(null,self__.ch,val,fn1);
});

cljs.core.async.t_cljs$core$async31726.getBasis = (function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.with_meta(new cljs.core.Symbol(null,"map<","map<",-1235808357,null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"arglists","arglists",1661989754),cljs.core.list(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.list(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null)], null))),new cljs.core.Keyword(null,"doc","doc",1913296891),"Deprecated - this function will be removed. Use transducer instead"], null)),new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta31727","meta31727",1198028729,null)], null);
});

cljs.core.async.t_cljs$core$async31726.cljs$lang$type = true;

cljs.core.async.t_cljs$core$async31726.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async31726";

cljs.core.async.t_cljs$core$async31726.cljs$lang$ctorPrWriter = (function (this__25396__auto__,writer__25397__auto__,opt__25398__auto__){
return cljs.core._write.call(null,writer__25397__auto__,"cljs.core.async/t_cljs$core$async31726");
});

cljs.core.async.__GT_t_cljs$core$async31726 = (function cljs$core$async$map_LT__$___GT_t_cljs$core$async31726(map_LT___$1,f__$1,ch__$1,meta31727){
return (new cljs.core.async.t_cljs$core$async31726(map_LT___$1,f__$1,ch__$1,meta31727));
});

}

return (new cljs.core.async.t_cljs$core$async31726(cljs$core$async$map_LT_,f,ch,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.map_GT_ = (function cljs$core$async$map_GT_(f,ch){
if(typeof cljs.core.async.t_cljs$core$async31735 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Channel}
 * @implements {cljs.core.async.impl.protocols.WritePort}
 * @implements {cljs.core.async.impl.protocols.ReadPort}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async31735 = (function (map_GT_,f,ch,meta31736){
this.map_GT_ = map_GT_;
this.f = f;
this.ch = ch;
this.meta31736 = meta31736;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
cljs.core.async.t_cljs$core$async31735.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_31737,meta31736__$1){
var self__ = this;
var _31737__$1 = this;
return (new cljs.core.async.t_cljs$core$async31735(self__.map_GT_,self__.f,self__.ch,meta31736__$1));
});

cljs.core.async.t_cljs$core$async31735.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_31737){
var self__ = this;
var _31737__$1 = this;
return self__.meta31736;
});

cljs.core.async.t_cljs$core$async31735.prototype.cljs$core$async$impl$protocols$Channel$ = true;

cljs.core.async.t_cljs$core$async31735.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_.call(null,self__.ch);
});

cljs.core.async.t_cljs$core$async31735.prototype.cljs$core$async$impl$protocols$ReadPort$ = true;

cljs.core.async.t_cljs$core$async31735.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.take_BANG_.call(null,self__.ch,fn1);
});

cljs.core.async.t_cljs$core$async31735.prototype.cljs$core$async$impl$protocols$WritePort$ = true;

cljs.core.async.t_cljs$core$async31735.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.put_BANG_.call(null,self__.ch,self__.f.call(null,val),fn1);
});

cljs.core.async.t_cljs$core$async31735.getBasis = (function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.with_meta(new cljs.core.Symbol(null,"map>","map>",1676369295,null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"arglists","arglists",1661989754),cljs.core.list(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.list(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null)], null))),new cljs.core.Keyword(null,"doc","doc",1913296891),"Deprecated - this function will be removed. Use transducer instead"], null)),new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta31736","meta31736",59397799,null)], null);
});

cljs.core.async.t_cljs$core$async31735.cljs$lang$type = true;

cljs.core.async.t_cljs$core$async31735.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async31735";

cljs.core.async.t_cljs$core$async31735.cljs$lang$ctorPrWriter = (function (this__25396__auto__,writer__25397__auto__,opt__25398__auto__){
return cljs.core._write.call(null,writer__25397__auto__,"cljs.core.async/t_cljs$core$async31735");
});

cljs.core.async.__GT_t_cljs$core$async31735 = (function cljs$core$async$map_GT__$___GT_t_cljs$core$async31735(map_GT___$1,f__$1,ch__$1,meta31736){
return (new cljs.core.async.t_cljs$core$async31735(map_GT___$1,f__$1,ch__$1,meta31736));
});

}

return (new cljs.core.async.t_cljs$core$async31735(cljs$core$async$map_GT_,f,ch,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.filter_GT_ = (function cljs$core$async$filter_GT_(p,ch){
if(typeof cljs.core.async.t_cljs$core$async31741 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Channel}
 * @implements {cljs.core.async.impl.protocols.WritePort}
 * @implements {cljs.core.async.impl.protocols.ReadPort}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async31741 = (function (filter_GT_,p,ch,meta31742){
this.filter_GT_ = filter_GT_;
this.p = p;
this.ch = ch;
this.meta31742 = meta31742;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
cljs.core.async.t_cljs$core$async31741.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_31743,meta31742__$1){
var self__ = this;
var _31743__$1 = this;
return (new cljs.core.async.t_cljs$core$async31741(self__.filter_GT_,self__.p,self__.ch,meta31742__$1));
});

cljs.core.async.t_cljs$core$async31741.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_31743){
var self__ = this;
var _31743__$1 = this;
return self__.meta31742;
});

cljs.core.async.t_cljs$core$async31741.prototype.cljs$core$async$impl$protocols$Channel$ = true;

cljs.core.async.t_cljs$core$async31741.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_.call(null,self__.ch);
});

cljs.core.async.t_cljs$core$async31741.prototype.cljs$core$async$impl$protocols$Channel$closed_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.closed_QMARK_.call(null,self__.ch);
});

cljs.core.async.t_cljs$core$async31741.prototype.cljs$core$async$impl$protocols$ReadPort$ = true;

cljs.core.async.t_cljs$core$async31741.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.take_BANG_.call(null,self__.ch,fn1);
});

cljs.core.async.t_cljs$core$async31741.prototype.cljs$core$async$impl$protocols$WritePort$ = true;

cljs.core.async.t_cljs$core$async31741.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
if(cljs.core.truth_(self__.p.call(null,val))){
return cljs.core.async.impl.protocols.put_BANG_.call(null,self__.ch,val,fn1);
} else {
return cljs.core.async.impl.channels.box.call(null,cljs.core.not.call(null,cljs.core.async.impl.protocols.closed_QMARK_.call(null,self__.ch)));
}
});

cljs.core.async.t_cljs$core$async31741.getBasis = (function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.with_meta(new cljs.core.Symbol(null,"filter>","filter>",-37644455,null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"arglists","arglists",1661989754),cljs.core.list(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.list(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"p","p",1791580836,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null)], null))),new cljs.core.Keyword(null,"doc","doc",1913296891),"Deprecated - this function will be removed. Use transducer instead"], null)),new cljs.core.Symbol(null,"p","p",1791580836,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta31742","meta31742",-833623526,null)], null);
});

cljs.core.async.t_cljs$core$async31741.cljs$lang$type = true;

cljs.core.async.t_cljs$core$async31741.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async31741";

cljs.core.async.t_cljs$core$async31741.cljs$lang$ctorPrWriter = (function (this__25396__auto__,writer__25397__auto__,opt__25398__auto__){
return cljs.core._write.call(null,writer__25397__auto__,"cljs.core.async/t_cljs$core$async31741");
});

cljs.core.async.__GT_t_cljs$core$async31741 = (function cljs$core$async$filter_GT__$___GT_t_cljs$core$async31741(filter_GT___$1,p__$1,ch__$1,meta31742){
return (new cljs.core.async.t_cljs$core$async31741(filter_GT___$1,p__$1,ch__$1,meta31742));
});

}

return (new cljs.core.async.t_cljs$core$async31741(cljs$core$async$filter_GT_,p,ch,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.remove_GT_ = (function cljs$core$async$remove_GT_(p,ch){
return cljs.core.async.filter_GT_.call(null,cljs.core.complement.call(null,p),ch);
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.filter_LT_ = (function cljs$core$async$filter_LT_(var_args){
var args31744 = [];
var len__25865__auto___31788 = arguments.length;
var i__25866__auto___31789 = (0);
while(true){
if((i__25866__auto___31789 < len__25865__auto___31788)){
args31744.push((arguments[i__25866__auto___31789]));

var G__31790 = (i__25866__auto___31789 + (1));
i__25866__auto___31789 = G__31790;
continue;
} else {
}
break;
}

var G__31746 = args31744.length;
switch (G__31746) {
case 2:
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args31744.length)].join('')));

}
});

cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$2 = (function (p,ch){
return cljs.core.async.filter_LT_.call(null,p,ch,null);
});

cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3 = (function (p,ch,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
var c__29520__auto___31792 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___31792,out){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___31792,out){
return (function (state_31767){
var state_val_31768 = (state_31767[(1)]);
if((state_val_31768 === (7))){
var inst_31763 = (state_31767[(2)]);
var state_31767__$1 = state_31767;
var statearr_31769_31793 = state_31767__$1;
(statearr_31769_31793[(2)] = inst_31763);

(statearr_31769_31793[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31768 === (1))){
var state_31767__$1 = state_31767;
var statearr_31770_31794 = state_31767__$1;
(statearr_31770_31794[(2)] = null);

(statearr_31770_31794[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31768 === (4))){
var inst_31749 = (state_31767[(7)]);
var inst_31749__$1 = (state_31767[(2)]);
var inst_31750 = (inst_31749__$1 == null);
var state_31767__$1 = (function (){var statearr_31771 = state_31767;
(statearr_31771[(7)] = inst_31749__$1);

return statearr_31771;
})();
if(cljs.core.truth_(inst_31750)){
var statearr_31772_31795 = state_31767__$1;
(statearr_31772_31795[(1)] = (5));

} else {
var statearr_31773_31796 = state_31767__$1;
(statearr_31773_31796[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31768 === (6))){
var inst_31749 = (state_31767[(7)]);
var inst_31754 = p.call(null,inst_31749);
var state_31767__$1 = state_31767;
if(cljs.core.truth_(inst_31754)){
var statearr_31774_31797 = state_31767__$1;
(statearr_31774_31797[(1)] = (8));

} else {
var statearr_31775_31798 = state_31767__$1;
(statearr_31775_31798[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31768 === (3))){
var inst_31765 = (state_31767[(2)]);
var state_31767__$1 = state_31767;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_31767__$1,inst_31765);
} else {
if((state_val_31768 === (2))){
var state_31767__$1 = state_31767;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_31767__$1,(4),ch);
} else {
if((state_val_31768 === (11))){
var inst_31757 = (state_31767[(2)]);
var state_31767__$1 = state_31767;
var statearr_31776_31799 = state_31767__$1;
(statearr_31776_31799[(2)] = inst_31757);

(statearr_31776_31799[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31768 === (9))){
var state_31767__$1 = state_31767;
var statearr_31777_31800 = state_31767__$1;
(statearr_31777_31800[(2)] = null);

(statearr_31777_31800[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31768 === (5))){
var inst_31752 = cljs.core.async.close_BANG_.call(null,out);
var state_31767__$1 = state_31767;
var statearr_31778_31801 = state_31767__$1;
(statearr_31778_31801[(2)] = inst_31752);

(statearr_31778_31801[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31768 === (10))){
var inst_31760 = (state_31767[(2)]);
var state_31767__$1 = (function (){var statearr_31779 = state_31767;
(statearr_31779[(8)] = inst_31760);

return statearr_31779;
})();
var statearr_31780_31802 = state_31767__$1;
(statearr_31780_31802[(2)] = null);

(statearr_31780_31802[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31768 === (8))){
var inst_31749 = (state_31767[(7)]);
var state_31767__$1 = state_31767;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_31767__$1,(11),out,inst_31749);
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
});})(c__29520__auto___31792,out))
;
return ((function (switch__29408__auto__,c__29520__auto___31792,out){
return (function() {
var cljs$core$async$state_machine__29409__auto__ = null;
var cljs$core$async$state_machine__29409__auto____0 = (function (){
var statearr_31784 = [null,null,null,null,null,null,null,null,null];
(statearr_31784[(0)] = cljs$core$async$state_machine__29409__auto__);

(statearr_31784[(1)] = (1));

return statearr_31784;
});
var cljs$core$async$state_machine__29409__auto____1 = (function (state_31767){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_31767);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e31785){if((e31785 instanceof Object)){
var ex__29412__auto__ = e31785;
var statearr_31786_31803 = state_31767;
(statearr_31786_31803[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_31767);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e31785;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__31804 = state_31767;
state_31767 = G__31804;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$state_machine__29409__auto__ = function(state_31767){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29409__auto____1.call(this,state_31767);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29409__auto____0;
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29409__auto____1;
return cljs$core$async$state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___31792,out))
})();
var state__29522__auto__ = (function (){var statearr_31787 = f__29521__auto__.call(null);
(statearr_31787[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___31792);

return statearr_31787;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___31792,out))
);


return out;
});

cljs.core.async.filter_LT_.cljs$lang$maxFixedArity = 3;

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.remove_LT_ = (function cljs$core$async$remove_LT_(var_args){
var args31805 = [];
var len__25865__auto___31808 = arguments.length;
var i__25866__auto___31809 = (0);
while(true){
if((i__25866__auto___31809 < len__25865__auto___31808)){
args31805.push((arguments[i__25866__auto___31809]));

var G__31810 = (i__25866__auto___31809 + (1));
i__25866__auto___31809 = G__31810;
continue;
} else {
}
break;
}

var G__31807 = args31805.length;
switch (G__31807) {
case 2:
return cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args31805.length)].join('')));

}
});

cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$2 = (function (p,ch){
return cljs.core.async.remove_LT_.call(null,p,ch,null);
});

cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$3 = (function (p,ch,buf_or_n){
return cljs.core.async.filter_LT_.call(null,cljs.core.complement.call(null,p),ch,buf_or_n);
});

cljs.core.async.remove_LT_.cljs$lang$maxFixedArity = 3;

cljs.core.async.mapcat_STAR_ = (function cljs$core$async$mapcat_STAR_(f,in$,out){
var c__29520__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto__){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto__){
return (function (state_31977){
var state_val_31978 = (state_31977[(1)]);
if((state_val_31978 === (7))){
var inst_31973 = (state_31977[(2)]);
var state_31977__$1 = state_31977;
var statearr_31979_32020 = state_31977__$1;
(statearr_31979_32020[(2)] = inst_31973);

(statearr_31979_32020[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (20))){
var inst_31943 = (state_31977[(7)]);
var inst_31954 = (state_31977[(2)]);
var inst_31955 = cljs.core.next.call(null,inst_31943);
var inst_31929 = inst_31955;
var inst_31930 = null;
var inst_31931 = (0);
var inst_31932 = (0);
var state_31977__$1 = (function (){var statearr_31980 = state_31977;
(statearr_31980[(8)] = inst_31954);

(statearr_31980[(9)] = inst_31929);

(statearr_31980[(10)] = inst_31931);

(statearr_31980[(11)] = inst_31932);

(statearr_31980[(12)] = inst_31930);

return statearr_31980;
})();
var statearr_31981_32021 = state_31977__$1;
(statearr_31981_32021[(2)] = null);

(statearr_31981_32021[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (1))){
var state_31977__$1 = state_31977;
var statearr_31982_32022 = state_31977__$1;
(statearr_31982_32022[(2)] = null);

(statearr_31982_32022[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (4))){
var inst_31918 = (state_31977[(13)]);
var inst_31918__$1 = (state_31977[(2)]);
var inst_31919 = (inst_31918__$1 == null);
var state_31977__$1 = (function (){var statearr_31983 = state_31977;
(statearr_31983[(13)] = inst_31918__$1);

return statearr_31983;
})();
if(cljs.core.truth_(inst_31919)){
var statearr_31984_32023 = state_31977__$1;
(statearr_31984_32023[(1)] = (5));

} else {
var statearr_31985_32024 = state_31977__$1;
(statearr_31985_32024[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (15))){
var state_31977__$1 = state_31977;
var statearr_31989_32025 = state_31977__$1;
(statearr_31989_32025[(2)] = null);

(statearr_31989_32025[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (21))){
var state_31977__$1 = state_31977;
var statearr_31990_32026 = state_31977__$1;
(statearr_31990_32026[(2)] = null);

(statearr_31990_32026[(1)] = (23));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (13))){
var inst_31929 = (state_31977[(9)]);
var inst_31931 = (state_31977[(10)]);
var inst_31932 = (state_31977[(11)]);
var inst_31930 = (state_31977[(12)]);
var inst_31939 = (state_31977[(2)]);
var inst_31940 = (inst_31932 + (1));
var tmp31986 = inst_31929;
var tmp31987 = inst_31931;
var tmp31988 = inst_31930;
var inst_31929__$1 = tmp31986;
var inst_31930__$1 = tmp31988;
var inst_31931__$1 = tmp31987;
var inst_31932__$1 = inst_31940;
var state_31977__$1 = (function (){var statearr_31991 = state_31977;
(statearr_31991[(9)] = inst_31929__$1);

(statearr_31991[(14)] = inst_31939);

(statearr_31991[(10)] = inst_31931__$1);

(statearr_31991[(11)] = inst_31932__$1);

(statearr_31991[(12)] = inst_31930__$1);

return statearr_31991;
})();
var statearr_31992_32027 = state_31977__$1;
(statearr_31992_32027[(2)] = null);

(statearr_31992_32027[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (22))){
var state_31977__$1 = state_31977;
var statearr_31993_32028 = state_31977__$1;
(statearr_31993_32028[(2)] = null);

(statearr_31993_32028[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (6))){
var inst_31918 = (state_31977[(13)]);
var inst_31927 = f.call(null,inst_31918);
var inst_31928 = cljs.core.seq.call(null,inst_31927);
var inst_31929 = inst_31928;
var inst_31930 = null;
var inst_31931 = (0);
var inst_31932 = (0);
var state_31977__$1 = (function (){var statearr_31994 = state_31977;
(statearr_31994[(9)] = inst_31929);

(statearr_31994[(10)] = inst_31931);

(statearr_31994[(11)] = inst_31932);

(statearr_31994[(12)] = inst_31930);

return statearr_31994;
})();
var statearr_31995_32029 = state_31977__$1;
(statearr_31995_32029[(2)] = null);

(statearr_31995_32029[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (17))){
var inst_31943 = (state_31977[(7)]);
var inst_31947 = cljs.core.chunk_first.call(null,inst_31943);
var inst_31948 = cljs.core.chunk_rest.call(null,inst_31943);
var inst_31949 = cljs.core.count.call(null,inst_31947);
var inst_31929 = inst_31948;
var inst_31930 = inst_31947;
var inst_31931 = inst_31949;
var inst_31932 = (0);
var state_31977__$1 = (function (){var statearr_31996 = state_31977;
(statearr_31996[(9)] = inst_31929);

(statearr_31996[(10)] = inst_31931);

(statearr_31996[(11)] = inst_31932);

(statearr_31996[(12)] = inst_31930);

return statearr_31996;
})();
var statearr_31997_32030 = state_31977__$1;
(statearr_31997_32030[(2)] = null);

(statearr_31997_32030[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (3))){
var inst_31975 = (state_31977[(2)]);
var state_31977__$1 = state_31977;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_31977__$1,inst_31975);
} else {
if((state_val_31978 === (12))){
var inst_31963 = (state_31977[(2)]);
var state_31977__$1 = state_31977;
var statearr_31998_32031 = state_31977__$1;
(statearr_31998_32031[(2)] = inst_31963);

(statearr_31998_32031[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (2))){
var state_31977__$1 = state_31977;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_31977__$1,(4),in$);
} else {
if((state_val_31978 === (23))){
var inst_31971 = (state_31977[(2)]);
var state_31977__$1 = state_31977;
var statearr_31999_32032 = state_31977__$1;
(statearr_31999_32032[(2)] = inst_31971);

(statearr_31999_32032[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (19))){
var inst_31958 = (state_31977[(2)]);
var state_31977__$1 = state_31977;
var statearr_32000_32033 = state_31977__$1;
(statearr_32000_32033[(2)] = inst_31958);

(statearr_32000_32033[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (11))){
var inst_31929 = (state_31977[(9)]);
var inst_31943 = (state_31977[(7)]);
var inst_31943__$1 = cljs.core.seq.call(null,inst_31929);
var state_31977__$1 = (function (){var statearr_32001 = state_31977;
(statearr_32001[(7)] = inst_31943__$1);

return statearr_32001;
})();
if(inst_31943__$1){
var statearr_32002_32034 = state_31977__$1;
(statearr_32002_32034[(1)] = (14));

} else {
var statearr_32003_32035 = state_31977__$1;
(statearr_32003_32035[(1)] = (15));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (9))){
var inst_31965 = (state_31977[(2)]);
var inst_31966 = cljs.core.async.impl.protocols.closed_QMARK_.call(null,out);
var state_31977__$1 = (function (){var statearr_32004 = state_31977;
(statearr_32004[(15)] = inst_31965);

return statearr_32004;
})();
if(cljs.core.truth_(inst_31966)){
var statearr_32005_32036 = state_31977__$1;
(statearr_32005_32036[(1)] = (21));

} else {
var statearr_32006_32037 = state_31977__$1;
(statearr_32006_32037[(1)] = (22));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (5))){
var inst_31921 = cljs.core.async.close_BANG_.call(null,out);
var state_31977__$1 = state_31977;
var statearr_32007_32038 = state_31977__$1;
(statearr_32007_32038[(2)] = inst_31921);

(statearr_32007_32038[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (14))){
var inst_31943 = (state_31977[(7)]);
var inst_31945 = cljs.core.chunked_seq_QMARK_.call(null,inst_31943);
var state_31977__$1 = state_31977;
if(inst_31945){
var statearr_32008_32039 = state_31977__$1;
(statearr_32008_32039[(1)] = (17));

} else {
var statearr_32009_32040 = state_31977__$1;
(statearr_32009_32040[(1)] = (18));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (16))){
var inst_31961 = (state_31977[(2)]);
var state_31977__$1 = state_31977;
var statearr_32010_32041 = state_31977__$1;
(statearr_32010_32041[(2)] = inst_31961);

(statearr_32010_32041[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31978 === (10))){
var inst_31932 = (state_31977[(11)]);
var inst_31930 = (state_31977[(12)]);
var inst_31937 = cljs.core._nth.call(null,inst_31930,inst_31932);
var state_31977__$1 = state_31977;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_31977__$1,(13),out,inst_31937);
} else {
if((state_val_31978 === (18))){
var inst_31943 = (state_31977[(7)]);
var inst_31952 = cljs.core.first.call(null,inst_31943);
var state_31977__$1 = state_31977;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_31977__$1,(20),out,inst_31952);
} else {
if((state_val_31978 === (8))){
var inst_31931 = (state_31977[(10)]);
var inst_31932 = (state_31977[(11)]);
var inst_31934 = (inst_31932 < inst_31931);
var inst_31935 = inst_31934;
var state_31977__$1 = state_31977;
if(cljs.core.truth_(inst_31935)){
var statearr_32011_32042 = state_31977__$1;
(statearr_32011_32042[(1)] = (10));

} else {
var statearr_32012_32043 = state_31977__$1;
(statearr_32012_32043[(1)] = (11));

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
});})(c__29520__auto__))
;
return ((function (switch__29408__auto__,c__29520__auto__){
return (function() {
var cljs$core$async$mapcat_STAR__$_state_machine__29409__auto__ = null;
var cljs$core$async$mapcat_STAR__$_state_machine__29409__auto____0 = (function (){
var statearr_32016 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_32016[(0)] = cljs$core$async$mapcat_STAR__$_state_machine__29409__auto__);

(statearr_32016[(1)] = (1));

return statearr_32016;
});
var cljs$core$async$mapcat_STAR__$_state_machine__29409__auto____1 = (function (state_31977){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_31977);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e32017){if((e32017 instanceof Object)){
var ex__29412__auto__ = e32017;
var statearr_32018_32044 = state_31977;
(statearr_32018_32044[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_31977);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e32017;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__32045 = state_31977;
state_31977 = G__32045;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$mapcat_STAR__$_state_machine__29409__auto__ = function(state_31977){
switch(arguments.length){
case 0:
return cljs$core$async$mapcat_STAR__$_state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$mapcat_STAR__$_state_machine__29409__auto____1.call(this,state_31977);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$mapcat_STAR__$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$mapcat_STAR__$_state_machine__29409__auto____0;
cljs$core$async$mapcat_STAR__$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$mapcat_STAR__$_state_machine__29409__auto____1;
return cljs$core$async$mapcat_STAR__$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto__))
})();
var state__29522__auto__ = (function (){var statearr_32019 = f__29521__auto__.call(null);
(statearr_32019[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto__);

return statearr_32019;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto__))
);

return c__29520__auto__;
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.mapcat_LT_ = (function cljs$core$async$mapcat_LT_(var_args){
var args32046 = [];
var len__25865__auto___32049 = arguments.length;
var i__25866__auto___32050 = (0);
while(true){
if((i__25866__auto___32050 < len__25865__auto___32049)){
args32046.push((arguments[i__25866__auto___32050]));

var G__32051 = (i__25866__auto___32050 + (1));
i__25866__auto___32050 = G__32051;
continue;
} else {
}
break;
}

var G__32048 = args32046.length;
switch (G__32048) {
case 2:
return cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args32046.length)].join('')));

}
});

cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$2 = (function (f,in$){
return cljs.core.async.mapcat_LT_.call(null,f,in$,null);
});

cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$3 = (function (f,in$,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
cljs.core.async.mapcat_STAR_.call(null,f,in$,out);

return out;
});

cljs.core.async.mapcat_LT_.cljs$lang$maxFixedArity = 3;

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.mapcat_GT_ = (function cljs$core$async$mapcat_GT_(var_args){
var args32053 = [];
var len__25865__auto___32056 = arguments.length;
var i__25866__auto___32057 = (0);
while(true){
if((i__25866__auto___32057 < len__25865__auto___32056)){
args32053.push((arguments[i__25866__auto___32057]));

var G__32058 = (i__25866__auto___32057 + (1));
i__25866__auto___32057 = G__32058;
continue;
} else {
}
break;
}

var G__32055 = args32053.length;
switch (G__32055) {
case 2:
return cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args32053.length)].join('')));

}
});

cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$2 = (function (f,out){
return cljs.core.async.mapcat_GT_.call(null,f,out,null);
});

cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$3 = (function (f,out,buf_or_n){
var in$ = cljs.core.async.chan.call(null,buf_or_n);
cljs.core.async.mapcat_STAR_.call(null,f,in$,out);

return in$;
});

cljs.core.async.mapcat_GT_.cljs$lang$maxFixedArity = 3;

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.unique = (function cljs$core$async$unique(var_args){
var args32060 = [];
var len__25865__auto___32111 = arguments.length;
var i__25866__auto___32112 = (0);
while(true){
if((i__25866__auto___32112 < len__25865__auto___32111)){
args32060.push((arguments[i__25866__auto___32112]));

var G__32113 = (i__25866__auto___32112 + (1));
i__25866__auto___32112 = G__32113;
continue;
} else {
}
break;
}

var G__32062 = args32060.length;
switch (G__32062) {
case 1:
return cljs.core.async.unique.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.unique.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args32060.length)].join('')));

}
});

cljs.core.async.unique.cljs$core$IFn$_invoke$arity$1 = (function (ch){
return cljs.core.async.unique.call(null,ch,null);
});

cljs.core.async.unique.cljs$core$IFn$_invoke$arity$2 = (function (ch,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
var c__29520__auto___32115 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___32115,out){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___32115,out){
return (function (state_32086){
var state_val_32087 = (state_32086[(1)]);
if((state_val_32087 === (7))){
var inst_32081 = (state_32086[(2)]);
var state_32086__$1 = state_32086;
var statearr_32088_32116 = state_32086__$1;
(statearr_32088_32116[(2)] = inst_32081);

(statearr_32088_32116[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32087 === (1))){
var inst_32063 = null;
var state_32086__$1 = (function (){var statearr_32089 = state_32086;
(statearr_32089[(7)] = inst_32063);

return statearr_32089;
})();
var statearr_32090_32117 = state_32086__$1;
(statearr_32090_32117[(2)] = null);

(statearr_32090_32117[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32087 === (4))){
var inst_32066 = (state_32086[(8)]);
var inst_32066__$1 = (state_32086[(2)]);
var inst_32067 = (inst_32066__$1 == null);
var inst_32068 = cljs.core.not.call(null,inst_32067);
var state_32086__$1 = (function (){var statearr_32091 = state_32086;
(statearr_32091[(8)] = inst_32066__$1);

return statearr_32091;
})();
if(inst_32068){
var statearr_32092_32118 = state_32086__$1;
(statearr_32092_32118[(1)] = (5));

} else {
var statearr_32093_32119 = state_32086__$1;
(statearr_32093_32119[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32087 === (6))){
var state_32086__$1 = state_32086;
var statearr_32094_32120 = state_32086__$1;
(statearr_32094_32120[(2)] = null);

(statearr_32094_32120[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32087 === (3))){
var inst_32083 = (state_32086[(2)]);
var inst_32084 = cljs.core.async.close_BANG_.call(null,out);
var state_32086__$1 = (function (){var statearr_32095 = state_32086;
(statearr_32095[(9)] = inst_32083);

return statearr_32095;
})();
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_32086__$1,inst_32084);
} else {
if((state_val_32087 === (2))){
var state_32086__$1 = state_32086;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_32086__$1,(4),ch);
} else {
if((state_val_32087 === (11))){
var inst_32066 = (state_32086[(8)]);
var inst_32075 = (state_32086[(2)]);
var inst_32063 = inst_32066;
var state_32086__$1 = (function (){var statearr_32096 = state_32086;
(statearr_32096[(10)] = inst_32075);

(statearr_32096[(7)] = inst_32063);

return statearr_32096;
})();
var statearr_32097_32121 = state_32086__$1;
(statearr_32097_32121[(2)] = null);

(statearr_32097_32121[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32087 === (9))){
var inst_32066 = (state_32086[(8)]);
var state_32086__$1 = state_32086;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_32086__$1,(11),out,inst_32066);
} else {
if((state_val_32087 === (5))){
var inst_32066 = (state_32086[(8)]);
var inst_32063 = (state_32086[(7)]);
var inst_32070 = cljs.core._EQ_.call(null,inst_32066,inst_32063);
var state_32086__$1 = state_32086;
if(inst_32070){
var statearr_32099_32122 = state_32086__$1;
(statearr_32099_32122[(1)] = (8));

} else {
var statearr_32100_32123 = state_32086__$1;
(statearr_32100_32123[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32087 === (10))){
var inst_32078 = (state_32086[(2)]);
var state_32086__$1 = state_32086;
var statearr_32101_32124 = state_32086__$1;
(statearr_32101_32124[(2)] = inst_32078);

(statearr_32101_32124[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32087 === (8))){
var inst_32063 = (state_32086[(7)]);
var tmp32098 = inst_32063;
var inst_32063__$1 = tmp32098;
var state_32086__$1 = (function (){var statearr_32102 = state_32086;
(statearr_32102[(7)] = inst_32063__$1);

return statearr_32102;
})();
var statearr_32103_32125 = state_32086__$1;
(statearr_32103_32125[(2)] = null);

(statearr_32103_32125[(1)] = (2));


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
});})(c__29520__auto___32115,out))
;
return ((function (switch__29408__auto__,c__29520__auto___32115,out){
return (function() {
var cljs$core$async$state_machine__29409__auto__ = null;
var cljs$core$async$state_machine__29409__auto____0 = (function (){
var statearr_32107 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_32107[(0)] = cljs$core$async$state_machine__29409__auto__);

(statearr_32107[(1)] = (1));

return statearr_32107;
});
var cljs$core$async$state_machine__29409__auto____1 = (function (state_32086){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_32086);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e32108){if((e32108 instanceof Object)){
var ex__29412__auto__ = e32108;
var statearr_32109_32126 = state_32086;
(statearr_32109_32126[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_32086);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e32108;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__32127 = state_32086;
state_32086 = G__32127;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$state_machine__29409__auto__ = function(state_32086){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29409__auto____1.call(this,state_32086);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29409__auto____0;
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29409__auto____1;
return cljs$core$async$state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___32115,out))
})();
var state__29522__auto__ = (function (){var statearr_32110 = f__29521__auto__.call(null);
(statearr_32110[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___32115);

return statearr_32110;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___32115,out))
);


return out;
});

cljs.core.async.unique.cljs$lang$maxFixedArity = 2;

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.partition = (function cljs$core$async$partition(var_args){
var args32128 = [];
var len__25865__auto___32198 = arguments.length;
var i__25866__auto___32199 = (0);
while(true){
if((i__25866__auto___32199 < len__25865__auto___32198)){
args32128.push((arguments[i__25866__auto___32199]));

var G__32200 = (i__25866__auto___32199 + (1));
i__25866__auto___32199 = G__32200;
continue;
} else {
}
break;
}

var G__32130 = args32128.length;
switch (G__32130) {
case 2:
return cljs.core.async.partition.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.partition.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args32128.length)].join('')));

}
});

cljs.core.async.partition.cljs$core$IFn$_invoke$arity$2 = (function (n,ch){
return cljs.core.async.partition.call(null,n,ch,null);
});

cljs.core.async.partition.cljs$core$IFn$_invoke$arity$3 = (function (n,ch,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
var c__29520__auto___32202 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___32202,out){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___32202,out){
return (function (state_32168){
var state_val_32169 = (state_32168[(1)]);
if((state_val_32169 === (7))){
var inst_32164 = (state_32168[(2)]);
var state_32168__$1 = state_32168;
var statearr_32170_32203 = state_32168__$1;
(statearr_32170_32203[(2)] = inst_32164);

(statearr_32170_32203[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32169 === (1))){
var inst_32131 = (new Array(n));
var inst_32132 = inst_32131;
var inst_32133 = (0);
var state_32168__$1 = (function (){var statearr_32171 = state_32168;
(statearr_32171[(7)] = inst_32132);

(statearr_32171[(8)] = inst_32133);

return statearr_32171;
})();
var statearr_32172_32204 = state_32168__$1;
(statearr_32172_32204[(2)] = null);

(statearr_32172_32204[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32169 === (4))){
var inst_32136 = (state_32168[(9)]);
var inst_32136__$1 = (state_32168[(2)]);
var inst_32137 = (inst_32136__$1 == null);
var inst_32138 = cljs.core.not.call(null,inst_32137);
var state_32168__$1 = (function (){var statearr_32173 = state_32168;
(statearr_32173[(9)] = inst_32136__$1);

return statearr_32173;
})();
if(inst_32138){
var statearr_32174_32205 = state_32168__$1;
(statearr_32174_32205[(1)] = (5));

} else {
var statearr_32175_32206 = state_32168__$1;
(statearr_32175_32206[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32169 === (15))){
var inst_32158 = (state_32168[(2)]);
var state_32168__$1 = state_32168;
var statearr_32176_32207 = state_32168__$1;
(statearr_32176_32207[(2)] = inst_32158);

(statearr_32176_32207[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32169 === (13))){
var state_32168__$1 = state_32168;
var statearr_32177_32208 = state_32168__$1;
(statearr_32177_32208[(2)] = null);

(statearr_32177_32208[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32169 === (6))){
var inst_32133 = (state_32168[(8)]);
var inst_32154 = (inst_32133 > (0));
var state_32168__$1 = state_32168;
if(cljs.core.truth_(inst_32154)){
var statearr_32178_32209 = state_32168__$1;
(statearr_32178_32209[(1)] = (12));

} else {
var statearr_32179_32210 = state_32168__$1;
(statearr_32179_32210[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32169 === (3))){
var inst_32166 = (state_32168[(2)]);
var state_32168__$1 = state_32168;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_32168__$1,inst_32166);
} else {
if((state_val_32169 === (12))){
var inst_32132 = (state_32168[(7)]);
var inst_32156 = cljs.core.vec.call(null,inst_32132);
var state_32168__$1 = state_32168;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_32168__$1,(15),out,inst_32156);
} else {
if((state_val_32169 === (2))){
var state_32168__$1 = state_32168;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_32168__$1,(4),ch);
} else {
if((state_val_32169 === (11))){
var inst_32148 = (state_32168[(2)]);
var inst_32149 = (new Array(n));
var inst_32132 = inst_32149;
var inst_32133 = (0);
var state_32168__$1 = (function (){var statearr_32180 = state_32168;
(statearr_32180[(7)] = inst_32132);

(statearr_32180[(10)] = inst_32148);

(statearr_32180[(8)] = inst_32133);

return statearr_32180;
})();
var statearr_32181_32211 = state_32168__$1;
(statearr_32181_32211[(2)] = null);

(statearr_32181_32211[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32169 === (9))){
var inst_32132 = (state_32168[(7)]);
var inst_32146 = cljs.core.vec.call(null,inst_32132);
var state_32168__$1 = state_32168;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_32168__$1,(11),out,inst_32146);
} else {
if((state_val_32169 === (5))){
var inst_32136 = (state_32168[(9)]);
var inst_32132 = (state_32168[(7)]);
var inst_32141 = (state_32168[(11)]);
var inst_32133 = (state_32168[(8)]);
var inst_32140 = (inst_32132[inst_32133] = inst_32136);
var inst_32141__$1 = (inst_32133 + (1));
var inst_32142 = (inst_32141__$1 < n);
var state_32168__$1 = (function (){var statearr_32182 = state_32168;
(statearr_32182[(12)] = inst_32140);

(statearr_32182[(11)] = inst_32141__$1);

return statearr_32182;
})();
if(cljs.core.truth_(inst_32142)){
var statearr_32183_32212 = state_32168__$1;
(statearr_32183_32212[(1)] = (8));

} else {
var statearr_32184_32213 = state_32168__$1;
(statearr_32184_32213[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32169 === (14))){
var inst_32161 = (state_32168[(2)]);
var inst_32162 = cljs.core.async.close_BANG_.call(null,out);
var state_32168__$1 = (function (){var statearr_32186 = state_32168;
(statearr_32186[(13)] = inst_32161);

return statearr_32186;
})();
var statearr_32187_32214 = state_32168__$1;
(statearr_32187_32214[(2)] = inst_32162);

(statearr_32187_32214[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32169 === (10))){
var inst_32152 = (state_32168[(2)]);
var state_32168__$1 = state_32168;
var statearr_32188_32215 = state_32168__$1;
(statearr_32188_32215[(2)] = inst_32152);

(statearr_32188_32215[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32169 === (8))){
var inst_32132 = (state_32168[(7)]);
var inst_32141 = (state_32168[(11)]);
var tmp32185 = inst_32132;
var inst_32132__$1 = tmp32185;
var inst_32133 = inst_32141;
var state_32168__$1 = (function (){var statearr_32189 = state_32168;
(statearr_32189[(7)] = inst_32132__$1);

(statearr_32189[(8)] = inst_32133);

return statearr_32189;
})();
var statearr_32190_32216 = state_32168__$1;
(statearr_32190_32216[(2)] = null);

(statearr_32190_32216[(1)] = (2));


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
});})(c__29520__auto___32202,out))
;
return ((function (switch__29408__auto__,c__29520__auto___32202,out){
return (function() {
var cljs$core$async$state_machine__29409__auto__ = null;
var cljs$core$async$state_machine__29409__auto____0 = (function (){
var statearr_32194 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_32194[(0)] = cljs$core$async$state_machine__29409__auto__);

(statearr_32194[(1)] = (1));

return statearr_32194;
});
var cljs$core$async$state_machine__29409__auto____1 = (function (state_32168){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_32168);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e32195){if((e32195 instanceof Object)){
var ex__29412__auto__ = e32195;
var statearr_32196_32217 = state_32168;
(statearr_32196_32217[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_32168);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e32195;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__32218 = state_32168;
state_32168 = G__32218;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$state_machine__29409__auto__ = function(state_32168){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29409__auto____1.call(this,state_32168);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29409__auto____0;
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29409__auto____1;
return cljs$core$async$state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___32202,out))
})();
var state__29522__auto__ = (function (){var statearr_32197 = f__29521__auto__.call(null);
(statearr_32197[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___32202);

return statearr_32197;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___32202,out))
);


return out;
});

cljs.core.async.partition.cljs$lang$maxFixedArity = 3;

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.partition_by = (function cljs$core$async$partition_by(var_args){
var args32219 = [];
var len__25865__auto___32293 = arguments.length;
var i__25866__auto___32294 = (0);
while(true){
if((i__25866__auto___32294 < len__25865__auto___32293)){
args32219.push((arguments[i__25866__auto___32294]));

var G__32295 = (i__25866__auto___32294 + (1));
i__25866__auto___32294 = G__32295;
continue;
} else {
}
break;
}

var G__32221 = args32219.length;
switch (G__32221) {
case 2:
return cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args32219.length)].join('')));

}
});

cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$2 = (function (f,ch){
return cljs.core.async.partition_by.call(null,f,ch,null);
});

cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$3 = (function (f,ch,buf_or_n){
var out = cljs.core.async.chan.call(null,buf_or_n);
var c__29520__auto___32297 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___32297,out){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___32297,out){
return (function (state_32263){
var state_val_32264 = (state_32263[(1)]);
if((state_val_32264 === (7))){
var inst_32259 = (state_32263[(2)]);
var state_32263__$1 = state_32263;
var statearr_32265_32298 = state_32263__$1;
(statearr_32265_32298[(2)] = inst_32259);

(statearr_32265_32298[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32264 === (1))){
var inst_32222 = [];
var inst_32223 = inst_32222;
var inst_32224 = new cljs.core.Keyword("cljs.core.async","nothing","cljs.core.async/nothing",-69252123);
var state_32263__$1 = (function (){var statearr_32266 = state_32263;
(statearr_32266[(7)] = inst_32224);

(statearr_32266[(8)] = inst_32223);

return statearr_32266;
})();
var statearr_32267_32299 = state_32263__$1;
(statearr_32267_32299[(2)] = null);

(statearr_32267_32299[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32264 === (4))){
var inst_32227 = (state_32263[(9)]);
var inst_32227__$1 = (state_32263[(2)]);
var inst_32228 = (inst_32227__$1 == null);
var inst_32229 = cljs.core.not.call(null,inst_32228);
var state_32263__$1 = (function (){var statearr_32268 = state_32263;
(statearr_32268[(9)] = inst_32227__$1);

return statearr_32268;
})();
if(inst_32229){
var statearr_32269_32300 = state_32263__$1;
(statearr_32269_32300[(1)] = (5));

} else {
var statearr_32270_32301 = state_32263__$1;
(statearr_32270_32301[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32264 === (15))){
var inst_32253 = (state_32263[(2)]);
var state_32263__$1 = state_32263;
var statearr_32271_32302 = state_32263__$1;
(statearr_32271_32302[(2)] = inst_32253);

(statearr_32271_32302[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32264 === (13))){
var state_32263__$1 = state_32263;
var statearr_32272_32303 = state_32263__$1;
(statearr_32272_32303[(2)] = null);

(statearr_32272_32303[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32264 === (6))){
var inst_32223 = (state_32263[(8)]);
var inst_32248 = inst_32223.length;
var inst_32249 = (inst_32248 > (0));
var state_32263__$1 = state_32263;
if(cljs.core.truth_(inst_32249)){
var statearr_32273_32304 = state_32263__$1;
(statearr_32273_32304[(1)] = (12));

} else {
var statearr_32274_32305 = state_32263__$1;
(statearr_32274_32305[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32264 === (3))){
var inst_32261 = (state_32263[(2)]);
var state_32263__$1 = state_32263;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_32263__$1,inst_32261);
} else {
if((state_val_32264 === (12))){
var inst_32223 = (state_32263[(8)]);
var inst_32251 = cljs.core.vec.call(null,inst_32223);
var state_32263__$1 = state_32263;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_32263__$1,(15),out,inst_32251);
} else {
if((state_val_32264 === (2))){
var state_32263__$1 = state_32263;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_32263__$1,(4),ch);
} else {
if((state_val_32264 === (11))){
var inst_32227 = (state_32263[(9)]);
var inst_32231 = (state_32263[(10)]);
var inst_32241 = (state_32263[(2)]);
var inst_32242 = [];
var inst_32243 = inst_32242.push(inst_32227);
var inst_32223 = inst_32242;
var inst_32224 = inst_32231;
var state_32263__$1 = (function (){var statearr_32275 = state_32263;
(statearr_32275[(7)] = inst_32224);

(statearr_32275[(8)] = inst_32223);

(statearr_32275[(11)] = inst_32243);

(statearr_32275[(12)] = inst_32241);

return statearr_32275;
})();
var statearr_32276_32306 = state_32263__$1;
(statearr_32276_32306[(2)] = null);

(statearr_32276_32306[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32264 === (9))){
var inst_32223 = (state_32263[(8)]);
var inst_32239 = cljs.core.vec.call(null,inst_32223);
var state_32263__$1 = state_32263;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_32263__$1,(11),out,inst_32239);
} else {
if((state_val_32264 === (5))){
var inst_32224 = (state_32263[(7)]);
var inst_32227 = (state_32263[(9)]);
var inst_32231 = (state_32263[(10)]);
var inst_32231__$1 = f.call(null,inst_32227);
var inst_32232 = cljs.core._EQ_.call(null,inst_32231__$1,inst_32224);
var inst_32233 = cljs.core.keyword_identical_QMARK_.call(null,inst_32224,new cljs.core.Keyword("cljs.core.async","nothing","cljs.core.async/nothing",-69252123));
var inst_32234 = (inst_32232) || (inst_32233);
var state_32263__$1 = (function (){var statearr_32277 = state_32263;
(statearr_32277[(10)] = inst_32231__$1);

return statearr_32277;
})();
if(cljs.core.truth_(inst_32234)){
var statearr_32278_32307 = state_32263__$1;
(statearr_32278_32307[(1)] = (8));

} else {
var statearr_32279_32308 = state_32263__$1;
(statearr_32279_32308[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32264 === (14))){
var inst_32256 = (state_32263[(2)]);
var inst_32257 = cljs.core.async.close_BANG_.call(null,out);
var state_32263__$1 = (function (){var statearr_32281 = state_32263;
(statearr_32281[(13)] = inst_32256);

return statearr_32281;
})();
var statearr_32282_32309 = state_32263__$1;
(statearr_32282_32309[(2)] = inst_32257);

(statearr_32282_32309[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32264 === (10))){
var inst_32246 = (state_32263[(2)]);
var state_32263__$1 = state_32263;
var statearr_32283_32310 = state_32263__$1;
(statearr_32283_32310[(2)] = inst_32246);

(statearr_32283_32310[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32264 === (8))){
var inst_32223 = (state_32263[(8)]);
var inst_32227 = (state_32263[(9)]);
var inst_32231 = (state_32263[(10)]);
var inst_32236 = inst_32223.push(inst_32227);
var tmp32280 = inst_32223;
var inst_32223__$1 = tmp32280;
var inst_32224 = inst_32231;
var state_32263__$1 = (function (){var statearr_32284 = state_32263;
(statearr_32284[(7)] = inst_32224);

(statearr_32284[(14)] = inst_32236);

(statearr_32284[(8)] = inst_32223__$1);

return statearr_32284;
})();
var statearr_32285_32311 = state_32263__$1;
(statearr_32285_32311[(2)] = null);

(statearr_32285_32311[(1)] = (2));


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
});})(c__29520__auto___32297,out))
;
return ((function (switch__29408__auto__,c__29520__auto___32297,out){
return (function() {
var cljs$core$async$state_machine__29409__auto__ = null;
var cljs$core$async$state_machine__29409__auto____0 = (function (){
var statearr_32289 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_32289[(0)] = cljs$core$async$state_machine__29409__auto__);

(statearr_32289[(1)] = (1));

return statearr_32289;
});
var cljs$core$async$state_machine__29409__auto____1 = (function (state_32263){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_32263);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e32290){if((e32290 instanceof Object)){
var ex__29412__auto__ = e32290;
var statearr_32291_32312 = state_32263;
(statearr_32291_32312[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_32263);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e32290;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__32313 = state_32263;
state_32263 = G__32313;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
cljs$core$async$state_machine__29409__auto__ = function(state_32263){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29409__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29409__auto____1.call(this,state_32263);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29409__auto____0;
cljs$core$async$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29409__auto____1;
return cljs$core$async$state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___32297,out))
})();
var state__29522__auto__ = (function (){var statearr_32292 = f__29521__auto__.call(null);
(statearr_32292[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___32297);

return statearr_32292;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___32297,out))
);


return out;
});

cljs.core.async.partition_by.cljs$lang$maxFixedArity = 3;


//# sourceMappingURL=async.js.map?rel=1485470171754