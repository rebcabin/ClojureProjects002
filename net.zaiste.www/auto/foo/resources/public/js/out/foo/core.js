// Compiled by ClojureScript 0.0-2511
goog.provide('foo.core');
goog.require('cljs.core');
goog.require('cljs_http.client');
goog.require('om.dom');
goog.require('om.core');
goog.require('cljs.core.async');
goog.require('goog.events');
cljs.core.enable_console_print_BANG_.call(null);
foo.core.fetch_widgets = (function fetch_widgets(url){
var c = cljs.core.async.chan.call(null);
var c__6505__auto___10357 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto___10357,c){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto___10357,c){
return (function (state_10343){
var state_val_10344 = (state_10343[(1)]);
if((state_val_10344 === (6))){
var inst_10341 = (state_10343[(2)]);
var state_10343__$1 = state_10343;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_10343__$1,inst_10341);
} else {
if((state_val_10344 === (5))){
var inst_10337 = (state_10343[(2)]);
var inst_10338 = cljs.core.get.call(null,inst_10337,new cljs.core.Keyword(null,"body","body",-2049205669));
var inst_10339 = cljs.core.vec.call(null,inst_10338);
var state_10343__$1 = state_10343;
return cljs.core.async.impl.ioc_helpers.put_BANG_.call(null,state_10343__$1,(6),c,inst_10339);
} else {
if((state_val_10344 === (4))){
var inst_10331 = (state_10343[(7)]);
var state_10343__$1 = state_10343;
var statearr_10345_10358 = state_10343__$1;
(statearr_10345_10358[(2)] = inst_10331);

(statearr_10345_10358[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_10344 === (3))){
var inst_10331 = (state_10343[(7)]);
var inst_10334 = cljs.core.apply.call(null,cljs.core.hash_map,inst_10331);
var state_10343__$1 = state_10343;
var statearr_10346_10359 = state_10343__$1;
(statearr_10346_10359[(2)] = inst_10334);

(statearr_10346_10359[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_10344 === (2))){
var inst_10331 = (state_10343[(7)]);
var inst_10331__$1 = (state_10343[(2)]);
var inst_10332 = cljs.core.seq_QMARK_.call(null,inst_10331__$1);
var state_10343__$1 = (function (){var statearr_10347 = state_10343;
(statearr_10347[(7)] = inst_10331__$1);

return statearr_10347;
})();
if(inst_10332){
var statearr_10348_10360 = state_10343__$1;
(statearr_10348_10360[(1)] = (3));

} else {
var statearr_10349_10361 = state_10343__$1;
(statearr_10349_10361[(1)] = (4));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_10344 === (1))){
var inst_10329 = cljs_http.client.get.call(null,url);
var state_10343__$1 = state_10343;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_10343__$1,(2),inst_10329);
} else {
return null;
}
}
}
}
}
}
});})(c__6505__auto___10357,c))
;
return ((function (switch__6490__auto__,c__6505__auto___10357,c){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_10353 = [null,null,null,null,null,null,null,null];
(statearr_10353[(0)] = state_machine__6491__auto__);

(statearr_10353[(1)] = (1));

return statearr_10353;
});
var state_machine__6491__auto____1 = (function (state_10343){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_10343);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e10354){if((e10354 instanceof Object)){
var ex__6494__auto__ = e10354;
var statearr_10355_10362 = state_10343;
(statearr_10355_10362[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_10343);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e10354;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__10363 = state_10343;
state_10343 = G__10363;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_10343){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_10343);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto___10357,c))
})();
var state__6507__auto__ = (function (){var statearr_10356 = f__6506__auto__.call(null);
(statearr_10356[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto___10357);

return statearr_10356;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto___10357,c))
);


return c;
});
foo.core.app_state = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
foo.core.widget = (function widget(p__10364,owner,opts){
var map__10369 = p__10364;
var map__10369__$1 = ((cljs.core.seq_QMARK_.call(null,map__10369))?cljs.core.apply.call(null,cljs.core.hash_map,map__10369):map__10369);
var name = cljs.core.get.call(null,map__10369__$1,new cljs.core.Keyword(null,"name","name",1843675177));
if(typeof foo.core.t10370 !== 'undefined'){
} else {

/**
* @constructor
*/
foo.core.t10370 = (function (name,map__10369,opts,owner,p__10364,widget,meta10371){
this.name = name;
this.map__10369 = map__10369;
this.opts = opts;
this.owner = owner;
this.p__10364 = p__10364;
this.widget = widget;
this.meta10371 = meta10371;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
foo.core.t10370.prototype.om$core$IRender$ = true;

foo.core.t10370.prototype.om$core$IRender$render$arity$1 = ((function (map__10369,map__10369__$1,name){
return (function (this__5277__auto__){
var self__ = this;
var this__5277__auto____$1 = this;
return React.DOM.li(null,self__.name);
});})(map__10369,map__10369__$1,name))
;

foo.core.t10370.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (map__10369,map__10369__$1,name){
return (function (_10372){
var self__ = this;
var _10372__$1 = this;
return self__.meta10371;
});})(map__10369,map__10369__$1,name))
;

foo.core.t10370.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (map__10369,map__10369__$1,name){
return (function (_10372,meta10371__$1){
var self__ = this;
var _10372__$1 = this;
return (new foo.core.t10370(self__.name,self__.map__10369,self__.opts,self__.owner,self__.p__10364,self__.widget,meta10371__$1));
});})(map__10369,map__10369__$1,name))
;

foo.core.t10370.cljs$lang$type = true;

foo.core.t10370.cljs$lang$ctorStr = "foo.core/t10370";

foo.core.t10370.cljs$lang$ctorPrWriter = ((function (map__10369,map__10369__$1,name){
return (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"foo.core/t10370");
});})(map__10369,map__10369__$1,name))
;

foo.core.__GT_t10370 = ((function (map__10369,map__10369__$1,name){
return (function __GT_t10370(name__$1,map__10369__$2,opts__$1,owner__$1,p__10364__$1,widget__$1,meta10371){
return (new foo.core.t10370(name__$1,map__10369__$2,opts__$1,owner__$1,p__10364__$1,widget__$1,meta10371));
});})(map__10369,map__10369__$1,name))
;

}

return (new foo.core.t10370(name,map__10369__$1,opts,owner,p__10364,widget,null));
});
foo.core.widget_list = (function widget_list(p__10373){
var map__10378 = p__10373;
var map__10378__$1 = ((cljs.core.seq_QMARK_.call(null,map__10378))?cljs.core.apply.call(null,cljs.core.hash_map,map__10378):map__10378);
var widgets = cljs.core.get.call(null,map__10378__$1,new cljs.core.Keyword(null,"widgets","widgets",-159098978));
if(typeof foo.core.t10379 !== 'undefined'){
} else {

/**
* @constructor
*/
foo.core.t10379 = (function (widgets,map__10378,p__10373,widget_list,meta10380){
this.widgets = widgets;
this.map__10378 = map__10378;
this.p__10373 = p__10373;
this.widget_list = widget_list;
this.meta10380 = meta10380;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
foo.core.t10379.prototype.om$core$IRender$ = true;

foo.core.t10379.prototype.om$core$IRender$render$arity$1 = ((function (map__10378,map__10378__$1,widgets){
return (function (this__5277__auto__){
var self__ = this;
var this__5277__auto____$1 = this;
return cljs.core.apply.call(null,om.dom.ul,null,om.core.build_all.call(null,foo.core.widget,self__.widgets));
});})(map__10378,map__10378__$1,widgets))
;

foo.core.t10379.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (map__10378,map__10378__$1,widgets){
return (function (_10381){
var self__ = this;
var _10381__$1 = this;
return self__.meta10380;
});})(map__10378,map__10378__$1,widgets))
;

foo.core.t10379.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (map__10378,map__10378__$1,widgets){
return (function (_10381,meta10380__$1){
var self__ = this;
var _10381__$1 = this;
return (new foo.core.t10379(self__.widgets,self__.map__10378,self__.p__10373,self__.widget_list,meta10380__$1));
});})(map__10378,map__10378__$1,widgets))
;

foo.core.t10379.cljs$lang$type = true;

foo.core.t10379.cljs$lang$ctorStr = "foo.core/t10379";

foo.core.t10379.cljs$lang$ctorPrWriter = ((function (map__10378,map__10378__$1,widgets){
return (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"foo.core/t10379");
});})(map__10378,map__10378__$1,widgets))
;

foo.core.__GT_t10379 = ((function (map__10378,map__10378__$1,widgets){
return (function __GT_t10379(widgets__$1,map__10378__$2,p__10373__$1,widget_list__$1,meta10380){
return (new foo.core.t10379(widgets__$1,map__10378__$2,p__10373__$1,widget_list__$1,meta10380));
});})(map__10378,map__10378__$1,widgets))
;

}

return (new foo.core.t10379(widgets,map__10378__$1,p__10373,widget_list,null));
});
foo.core.widget_box = (function widget_box(app,owner,opts){
if(typeof foo.core.t10425 !== 'undefined'){
} else {

/**
* @constructor
*/
foo.core.t10425 = (function (opts,owner,app,widget_box,meta10426){
this.opts = opts;
this.owner = owner;
this.app = app;
this.widget_box = widget_box;
this.meta10426 = meta10426;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
foo.core.t10425.prototype.om$core$IRender$ = true;

foo.core.t10425.prototype.om$core$IRender$render$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return React.DOM.div(null,React.DOM.h1(null,"Widgets"),om.core.build.call(null,foo.core.widget_list,self__.app));
});

foo.core.t10425.prototype.om$core$IWillMount$ = true;

foo.core.t10425.prototype.om$core$IWillMount$will_mount$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
om.core.transact_BANG_.call(null,self__.app,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"widgets","widgets",-159098978)], null),((function (___$1){
return (function (){
return cljs.core.PersistentVector.EMPTY;
});})(___$1))
);

var c__6505__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__6505__auto__,___$1){
return (function (){
var f__6506__auto__ = (function (){var switch__6490__auto__ = ((function (c__6505__auto__,___$1){
return (function (state_10451){
var state_val_10452 = (state_10451[(1)]);
if((state_val_10452 === (8))){
var inst_10443 = (state_10451[(2)]);
var state_10451__$1 = (function (){var statearr_10453 = state_10451;
(statearr_10453[(7)] = inst_10443);

return statearr_10453;
})();
var statearr_10454_10468 = state_10451__$1;
(statearr_10454_10468[(2)] = null);

(statearr_10454_10468[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_10452 === (7))){
var inst_10433 = (state_10451[(2)]);
var inst_10434 = cljs.core.pr_str.call(null,inst_10433);
var inst_10435 = console.log(inst_10434);
var inst_10436 = cljs.core.PersistentVector.EMPTY_NODE;
var inst_10437 = [new cljs.core.Keyword(null,"widgets","widgets",-159098978)];
var inst_10438 = (new cljs.core.PersistentVector(null,1,(5),inst_10436,inst_10437,null));
var inst_10439 = om.core.update_BANG_.call(null,self__.app,inst_10438,inst_10433);
var inst_10440 = new cljs.core.Keyword(null,"poll-interval","poll-interval",345867570).cljs$core$IFn$_invoke$arity$1(self__.opts);
var inst_10441 = cljs.core.async.timeout.call(null,inst_10440);
var state_10451__$1 = (function (){var statearr_10455 = state_10451;
(statearr_10455[(8)] = inst_10439);

(statearr_10455[(9)] = inst_10435);

return statearr_10455;
})();
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_10451__$1,(8),inst_10441);
} else {
if((state_val_10452 === (6))){
var inst_10447 = (state_10451[(2)]);
var state_10451__$1 = state_10451;
var statearr_10456_10469 = state_10451__$1;
(statearr_10456_10469[(2)] = inst_10447);

(statearr_10456_10469[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_10452 === (5))){
var state_10451__$1 = state_10451;
var statearr_10457_10470 = state_10451__$1;
(statearr_10457_10470[(2)] = null);

(statearr_10457_10470[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_10452 === (4))){
var inst_10430 = new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(self__.opts);
var inst_10431 = foo.core.fetch_widgets.call(null,inst_10430);
var state_10451__$1 = state_10451;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_10451__$1,(7),inst_10431);
} else {
if((state_val_10452 === (3))){
var inst_10449 = (state_10451[(2)]);
var state_10451__$1 = state_10451;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_10451__$1,inst_10449);
} else {
if((state_val_10452 === (2))){
var state_10451__$1 = state_10451;
var statearr_10458_10471 = state_10451__$1;
(statearr_10458_10471[(1)] = (4));



return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_10452 === (1))){
var state_10451__$1 = state_10451;
var statearr_10460_10472 = state_10451__$1;
(statearr_10460_10472[(2)] = null);

(statearr_10460_10472[(1)] = (2));


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
});})(c__6505__auto__,___$1))
;
return ((function (switch__6490__auto__,c__6505__auto__,___$1){
return (function() {
var state_machine__6491__auto__ = null;
var state_machine__6491__auto____0 = (function (){
var statearr_10464 = [null,null,null,null,null,null,null,null,null,null];
(statearr_10464[(0)] = state_machine__6491__auto__);

(statearr_10464[(1)] = (1));

return statearr_10464;
});
var state_machine__6491__auto____1 = (function (state_10451){
while(true){
var ret_value__6492__auto__ = (function (){try{while(true){
var result__6493__auto__ = switch__6490__auto__.call(null,state_10451);
if(cljs.core.keyword_identical_QMARK_.call(null,result__6493__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__6493__auto__;
}
break;
}
}catch (e10465){if((e10465 instanceof Object)){
var ex__6494__auto__ = e10465;
var statearr_10466_10473 = state_10451;
(statearr_10466_10473[(5)] = ex__6494__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_10451);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e10465;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__6492__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__10474 = state_10451;
state_10451 = G__10474;
continue;
} else {
return ret_value__6492__auto__;
}
break;
}
});
state_machine__6491__auto__ = function(state_10451){
switch(arguments.length){
case 0:
return state_machine__6491__auto____0.call(this);
case 1:
return state_machine__6491__auto____1.call(this,state_10451);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$0 = state_machine__6491__auto____0;
state_machine__6491__auto__.cljs$core$IFn$_invoke$arity$1 = state_machine__6491__auto____1;
return state_machine__6491__auto__;
})()
;})(switch__6490__auto__,c__6505__auto__,___$1))
})();
var state__6507__auto__ = (function (){var statearr_10467 = f__6506__auto__.call(null);
(statearr_10467[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__6505__auto__);

return statearr_10467;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__6507__auto__);
});})(c__6505__auto__,___$1))
);

return c__6505__auto__;
});

foo.core.t10425.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_10427){
var self__ = this;
var _10427__$1 = this;
return self__.meta10426;
});

foo.core.t10425.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_10427,meta10426__$1){
var self__ = this;
var _10427__$1 = this;
return (new foo.core.t10425(self__.opts,self__.owner,self__.app,self__.widget_box,meta10426__$1));
});

foo.core.t10425.cljs$lang$type = true;

foo.core.t10425.cljs$lang$ctorStr = "foo.core/t10425";

foo.core.t10425.cljs$lang$ctorPrWriter = (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"foo.core/t10425");
});

foo.core.__GT_t10425 = (function __GT_t10425(opts__$1,owner__$1,app__$1,widget_box__$1,meta10426){
return (new foo.core.t10425(opts__$1,owner__$1,app__$1,widget_box__$1,meta10426));
});

}

return (new foo.core.t10425(opts,owner,app,widget_box,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"line","line",212345235),31,new cljs.core.Keyword(null,"column","column",2078222095),3,new cljs.core.Keyword(null,"end-line","end-line",1837326455),46,new cljs.core.Keyword(null,"end-column","end-column",1425389514),51], null)));
});
foo.core.om_app = (function om_app(app,owner){
if(typeof foo.core.t10478 !== 'undefined'){
} else {

/**
* @constructor
*/
foo.core.t10478 = (function (owner,app,om_app,meta10479){
this.owner = owner;
this.app = app;
this.om_app = om_app;
this.meta10479 = meta10479;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
foo.core.t10478.prototype.om$core$IRender$ = true;

foo.core.t10478.prototype.om$core$IRender$render$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return React.DOM.div(null,om.core.build.call(null,foo.core.widget_box,self__.app,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"opts","opts",155075701),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"url","url",276297046),"/widgets",new cljs.core.Keyword(null,"poll-interval","poll-interval",345867570),(2000)], null)], null)));
});

foo.core.t10478.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_10480){
var self__ = this;
var _10480__$1 = this;
return self__.meta10479;
});

foo.core.t10478.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_10480,meta10479__$1){
var self__ = this;
var _10480__$1 = this;
return (new foo.core.t10478(self__.owner,self__.app,self__.om_app,meta10479__$1));
});

foo.core.t10478.cljs$lang$type = true;

foo.core.t10478.cljs$lang$ctorStr = "foo.core/t10478";

foo.core.t10478.cljs$lang$ctorPrWriter = (function (this__4217__auto__,writer__4218__auto__,opt__4219__auto__){
return cljs.core._write.call(null,writer__4218__auto__,"foo.core/t10478");
});

foo.core.__GT_t10478 = (function __GT_t10478(owner__$1,app__$1,om_app__$1,meta10479){
return (new foo.core.t10478(owner__$1,app__$1,om_app__$1,meta10479));
});

}

return (new foo.core.t10478(owner,app,om_app,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"line","line",212345235),49,new cljs.core.Keyword(null,"column","column",2078222095),3,new cljs.core.Keyword(null,"end-line","end-line",1837326455),55,new cljs.core.Keyword(null,"end-column","end-column",1425389514),65], null)));
});
om.core.root.call(null,foo.core.om_app,foo.core.app_state,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"target","target",253001721),document.getElementById("content")], null));

//# sourceMappingURL=core.js.map