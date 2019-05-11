// Compiled by ClojureScript 1.9.229 {}
goog.provide('figwheel.client');
goog.require('cljs.core');
goog.require('goog.userAgent.product');
goog.require('goog.Uri');
goog.require('cljs.core.async');
goog.require('goog.object');
goog.require('figwheel.client.socket');
goog.require('figwheel.client.file_reloading');
goog.require('clojure.string');
goog.require('figwheel.client.utils');
goog.require('cljs.repl');
goog.require('figwheel.client.heads_up');
goog.require('cljs.reader');
figwheel.client._figwheel_version_ = "0.5.8";
figwheel.client.figwheel_repl_print = (function figwheel$client$figwheel_repl_print(var_args){
var args36901 = [];
var len__25865__auto___36904 = arguments.length;
var i__25866__auto___36905 = (0);
while(true){
if((i__25866__auto___36905 < len__25865__auto___36904)){
args36901.push((arguments[i__25866__auto___36905]));

var G__36906 = (i__25866__auto___36905 + (1));
i__25866__auto___36905 = G__36906;
continue;
} else {
}
break;
}

var G__36903 = args36901.length;
switch (G__36903) {
case 2:
return figwheel.client.figwheel_repl_print.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 1:
return figwheel.client.figwheel_repl_print.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args36901.length)].join('')));

}
});

figwheel.client.figwheel_repl_print.cljs$core$IFn$_invoke$arity$2 = (function (stream,args){
figwheel.client.socket.send_BANG_.call(null,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"figwheel-event","figwheel-event",519570592),"callback",new cljs.core.Keyword(null,"callback-name","callback-name",336964714),"figwheel-repl-print",new cljs.core.Keyword(null,"content","content",15833224),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"stream","stream",1534941648),stream,new cljs.core.Keyword(null,"args","args",1315556576),args], null)], null));

return null;
});

figwheel.client.figwheel_repl_print.cljs$core$IFn$_invoke$arity$1 = (function (args){
return figwheel.client.figwheel_repl_print.call(null,new cljs.core.Keyword(null,"out","out",-910545517),args);
});

figwheel.client.figwheel_repl_print.cljs$lang$maxFixedArity = 2;

figwheel.client.console_out_print = (function figwheel$client$console_out_print(args){
return console.log.apply(console,cljs.core.into_array.call(null,args));
});
figwheel.client.console_err_print = (function figwheel$client$console_err_print(args){
return console.error.apply(console,cljs.core.into_array.call(null,args));
});
figwheel.client.repl_out_print_fn = (function figwheel$client$repl_out_print_fn(var_args){
var args__25872__auto__ = [];
var len__25865__auto___36909 = arguments.length;
var i__25866__auto___36910 = (0);
while(true){
if((i__25866__auto___36910 < len__25865__auto___36909)){
args__25872__auto__.push((arguments[i__25866__auto___36910]));

var G__36911 = (i__25866__auto___36910 + (1));
i__25866__auto___36910 = G__36911;
continue;
} else {
}
break;
}

var argseq__25873__auto__ = ((((0) < args__25872__auto__.length))?(new cljs.core.IndexedSeq(args__25872__auto__.slice((0)),(0),null)):null);
return figwheel.client.repl_out_print_fn.cljs$core$IFn$_invoke$arity$variadic(argseq__25873__auto__);
});

figwheel.client.repl_out_print_fn.cljs$core$IFn$_invoke$arity$variadic = (function (args){
figwheel.client.console_out_print.call(null,args);

figwheel.client.figwheel_repl_print.call(null,new cljs.core.Keyword(null,"out","out",-910545517),args);

return null;
});

figwheel.client.repl_out_print_fn.cljs$lang$maxFixedArity = (0);

figwheel.client.repl_out_print_fn.cljs$lang$applyTo = (function (seq36908){
return figwheel.client.repl_out_print_fn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq.call(null,seq36908));
});

figwheel.client.repl_err_print_fn = (function figwheel$client$repl_err_print_fn(var_args){
var args__25872__auto__ = [];
var len__25865__auto___36913 = arguments.length;
var i__25866__auto___36914 = (0);
while(true){
if((i__25866__auto___36914 < len__25865__auto___36913)){
args__25872__auto__.push((arguments[i__25866__auto___36914]));

var G__36915 = (i__25866__auto___36914 + (1));
i__25866__auto___36914 = G__36915;
continue;
} else {
}
break;
}

var argseq__25873__auto__ = ((((0) < args__25872__auto__.length))?(new cljs.core.IndexedSeq(args__25872__auto__.slice((0)),(0),null)):null);
return figwheel.client.repl_err_print_fn.cljs$core$IFn$_invoke$arity$variadic(argseq__25873__auto__);
});

figwheel.client.repl_err_print_fn.cljs$core$IFn$_invoke$arity$variadic = (function (args){
figwheel.client.console_err_print.call(null,args);

figwheel.client.figwheel_repl_print.call(null,new cljs.core.Keyword(null,"err","err",-2089457205),args);

return null;
});

figwheel.client.repl_err_print_fn.cljs$lang$maxFixedArity = (0);

figwheel.client.repl_err_print_fn.cljs$lang$applyTo = (function (seq36912){
return figwheel.client.repl_err_print_fn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq.call(null,seq36912));
});

figwheel.client.enable_repl_print_BANG_ = (function figwheel$client$enable_repl_print_BANG_(){
cljs.core._STAR_print_newline_STAR_ = false;

cljs.core.set_print_fn_BANG_.call(null,figwheel.client.repl_out_print_fn);

cljs.core.set_print_err_fn_BANG_.call(null,figwheel.client.repl_err_print_fn);

return null;
});
figwheel.client.autoload_QMARK_ = (cljs.core.truth_(figwheel.client.utils.html_env_QMARK_.call(null))?(function (){
var pred__36916 = cljs.core._EQ_;
var expr__36917 = (function (){var or__24790__auto__ = (function (){try{if(cljs.core.truth_(typeof localstorage !== 'undefined')){
return localStorage.getItem("figwheel_autoload");
} else {
return null;
}
}catch (e36920){if((e36920 instanceof Error)){
var e = e36920;
return false;
} else {
throw e36920;

}
}})();
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
return "true";
}
})();
if(cljs.core.truth_(pred__36916.call(null,"true",expr__36917))){
return true;
} else {
if(cljs.core.truth_(pred__36916.call(null,"false",expr__36917))){
return false;
} else {
throw (new Error([cljs.core.str("No matching clause: "),cljs.core.str(expr__36917)].join('')));
}
}
}):(function (){
return true;
}));
figwheel.client.toggle_autoload = (function figwheel$client$toggle_autoload(){
if(cljs.core.truth_(figwheel.client.utils.html_env_QMARK_.call(null))){
try{if(cljs.core.truth_(typeof localstorage !== 'undefined')){
localStorage.setItem("figwheel_autoload",cljs.core.not.call(null,figwheel.client.autoload_QMARK_.call(null)));
} else {
}

return figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"info","info",-317069002),[cljs.core.str("Figwheel autoloading "),cljs.core.str((cljs.core.truth_(figwheel.client.autoload_QMARK_.call(null))?"ON":"OFF"))].join(''));
}catch (e36922){if((e36922 instanceof Error)){
var e = e36922;
return figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"info","info",-317069002),[cljs.core.str("Unable to access localStorage")].join(''));
} else {
throw e36922;

}
}} else {
return null;
}
});
goog.exportSymbol('figwheel.client.toggle_autoload', figwheel.client.toggle_autoload);
figwheel.client.get_essential_messages = (function figwheel$client$get_essential_messages(ed){
if(cljs.core.truth_(ed)){
return cljs.core.cons.call(null,cljs.core.select_keys.call(null,ed,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"message","message",-406056002),new cljs.core.Keyword(null,"class","class",-2030961996)], null)),figwheel$client$get_essential_messages.call(null,new cljs.core.Keyword(null,"cause","cause",231901252).cljs$core$IFn$_invoke$arity$1(ed)));
} else {
return null;
}
});
figwheel.client.error_msg_format = (function figwheel$client$error_msg_format(p__36923){
var map__36926 = p__36923;
var map__36926__$1 = ((((!((map__36926 == null)))?((((map__36926.cljs$lang$protocol_mask$partition0$ & (64))) || (map__36926.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__36926):map__36926);
var message = cljs.core.get.call(null,map__36926__$1,new cljs.core.Keyword(null,"message","message",-406056002));
var class$ = cljs.core.get.call(null,map__36926__$1,new cljs.core.Keyword(null,"class","class",-2030961996));
return [cljs.core.str(class$),cljs.core.str(" : "),cljs.core.str(message)].join('');
});
figwheel.client.format_messages = cljs.core.comp.call(null,cljs.core.partial.call(null,cljs.core.map,figwheel.client.error_msg_format),figwheel.client.get_essential_messages);
figwheel.client.focus_msgs = (function figwheel$client$focus_msgs(name_set,msg_hist){
return cljs.core.cons.call(null,cljs.core.first.call(null,msg_hist),cljs.core.filter.call(null,cljs.core.comp.call(null,name_set,new cljs.core.Keyword(null,"msg-name","msg-name",-353709863)),cljs.core.rest.call(null,msg_hist)));
});
figwheel.client.reload_file_QMARK__STAR_ = (function figwheel$client$reload_file_QMARK__STAR_(msg_name,opts){
var or__24790__auto__ = new cljs.core.Keyword(null,"load-warninged-code","load-warninged-code",-2030345223).cljs$core$IFn$_invoke$arity$1(opts);
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
return cljs.core.not_EQ_.call(null,msg_name,new cljs.core.Keyword(null,"compile-warning","compile-warning",43425356));
}
});
figwheel.client.reload_file_state_QMARK_ = (function figwheel$client$reload_file_state_QMARK_(msg_names,opts){
var and__24778__auto__ = cljs.core._EQ_.call(null,cljs.core.first.call(null,msg_names),new cljs.core.Keyword(null,"files-changed","files-changed",-1418200563));
if(and__24778__auto__){
return figwheel.client.reload_file_QMARK__STAR_.call(null,cljs.core.second.call(null,msg_names),opts);
} else {
return and__24778__auto__;
}
});
figwheel.client.block_reload_file_state_QMARK_ = (function figwheel$client$block_reload_file_state_QMARK_(msg_names,opts){
return (cljs.core._EQ_.call(null,cljs.core.first.call(null,msg_names),new cljs.core.Keyword(null,"files-changed","files-changed",-1418200563))) && (cljs.core.not.call(null,figwheel.client.reload_file_QMARK__STAR_.call(null,cljs.core.second.call(null,msg_names),opts)));
});
figwheel.client.warning_append_state_QMARK_ = (function figwheel$client$warning_append_state_QMARK_(msg_names){
return cljs.core._EQ_.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"compile-warning","compile-warning",43425356),new cljs.core.Keyword(null,"compile-warning","compile-warning",43425356)], null),cljs.core.take.call(null,(2),msg_names));
});
figwheel.client.warning_state_QMARK_ = (function figwheel$client$warning_state_QMARK_(msg_names){
return cljs.core._EQ_.call(null,new cljs.core.Keyword(null,"compile-warning","compile-warning",43425356),cljs.core.first.call(null,msg_names));
});
figwheel.client.rewarning_state_QMARK_ = (function figwheel$client$rewarning_state_QMARK_(msg_names){
return cljs.core._EQ_.call(null,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"compile-warning","compile-warning",43425356),new cljs.core.Keyword(null,"files-changed","files-changed",-1418200563),new cljs.core.Keyword(null,"compile-warning","compile-warning",43425356)], null),cljs.core.take.call(null,(3),msg_names));
});
figwheel.client.compile_fail_state_QMARK_ = (function figwheel$client$compile_fail_state_QMARK_(msg_names){
return cljs.core._EQ_.call(null,new cljs.core.Keyword(null,"compile-failed","compile-failed",-477639289),cljs.core.first.call(null,msg_names));
});
figwheel.client.compile_refail_state_QMARK_ = (function figwheel$client$compile_refail_state_QMARK_(msg_names){
return cljs.core._EQ_.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"compile-failed","compile-failed",-477639289),new cljs.core.Keyword(null,"compile-failed","compile-failed",-477639289)], null),cljs.core.take.call(null,(2),msg_names));
});
figwheel.client.css_loaded_state_QMARK_ = (function figwheel$client$css_loaded_state_QMARK_(msg_names){
return cljs.core._EQ_.call(null,new cljs.core.Keyword(null,"css-files-changed","css-files-changed",720773874),cljs.core.first.call(null,msg_names));
});
figwheel.client.file_reloader_plugin = (function figwheel$client$file_reloader_plugin(opts){
var ch = cljs.core.async.chan.call(null);
var c__29520__auto___37088 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___37088,ch){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___37088,ch){
return (function (state_37057){
var state_val_37058 = (state_37057[(1)]);
if((state_val_37058 === (7))){
var inst_37053 = (state_37057[(2)]);
var state_37057__$1 = state_37057;
var statearr_37059_37089 = state_37057__$1;
(statearr_37059_37089[(2)] = inst_37053);

(statearr_37059_37089[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (1))){
var state_37057__$1 = state_37057;
var statearr_37060_37090 = state_37057__$1;
(statearr_37060_37090[(2)] = null);

(statearr_37060_37090[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (4))){
var inst_37010 = (state_37057[(7)]);
var inst_37010__$1 = (state_37057[(2)]);
var state_37057__$1 = (function (){var statearr_37061 = state_37057;
(statearr_37061[(7)] = inst_37010__$1);

return statearr_37061;
})();
if(cljs.core.truth_(inst_37010__$1)){
var statearr_37062_37091 = state_37057__$1;
(statearr_37062_37091[(1)] = (5));

} else {
var statearr_37063_37092 = state_37057__$1;
(statearr_37063_37092[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (15))){
var inst_37017 = (state_37057[(8)]);
var inst_37032 = new cljs.core.Keyword(null,"files","files",-472457450).cljs$core$IFn$_invoke$arity$1(inst_37017);
var inst_37033 = cljs.core.first.call(null,inst_37032);
var inst_37034 = new cljs.core.Keyword(null,"file","file",-1269645878).cljs$core$IFn$_invoke$arity$1(inst_37033);
var inst_37035 = [cljs.core.str("Figwheel: Not loading code with warnings - "),cljs.core.str(inst_37034)].join('');
var inst_37036 = figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"warn","warn",-436710552),inst_37035);
var state_37057__$1 = state_37057;
var statearr_37064_37093 = state_37057__$1;
(statearr_37064_37093[(2)] = inst_37036);

(statearr_37064_37093[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (13))){
var inst_37041 = (state_37057[(2)]);
var state_37057__$1 = state_37057;
var statearr_37065_37094 = state_37057__$1;
(statearr_37065_37094[(2)] = inst_37041);

(statearr_37065_37094[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (6))){
var state_37057__$1 = state_37057;
var statearr_37066_37095 = state_37057__$1;
(statearr_37066_37095[(2)] = null);

(statearr_37066_37095[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (17))){
var inst_37039 = (state_37057[(2)]);
var state_37057__$1 = state_37057;
var statearr_37067_37096 = state_37057__$1;
(statearr_37067_37096[(2)] = inst_37039);

(statearr_37067_37096[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (3))){
var inst_37055 = (state_37057[(2)]);
var state_37057__$1 = state_37057;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_37057__$1,inst_37055);
} else {
if((state_val_37058 === (12))){
var inst_37016 = (state_37057[(9)]);
var inst_37030 = figwheel.client.block_reload_file_state_QMARK_.call(null,inst_37016,opts);
var state_37057__$1 = state_37057;
if(cljs.core.truth_(inst_37030)){
var statearr_37068_37097 = state_37057__$1;
(statearr_37068_37097[(1)] = (15));

} else {
var statearr_37069_37098 = state_37057__$1;
(statearr_37069_37098[(1)] = (16));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (2))){
var state_37057__$1 = state_37057;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37057__$1,(4),ch);
} else {
if((state_val_37058 === (11))){
var inst_37017 = (state_37057[(8)]);
var inst_37022 = cljs.core.PersistentVector.EMPTY_NODE;
var inst_37023 = figwheel.client.file_reloading.reload_js_files.call(null,opts,inst_37017);
var inst_37024 = cljs.core.async.timeout.call(null,(1000));
var inst_37025 = [inst_37023,inst_37024];
var inst_37026 = (new cljs.core.PersistentVector(null,2,(5),inst_37022,inst_37025,null));
var state_37057__$1 = state_37057;
return cljs.core.async.ioc_alts_BANG_.call(null,state_37057__$1,(14),inst_37026);
} else {
if((state_val_37058 === (9))){
var inst_37017 = (state_37057[(8)]);
var inst_37043 = figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"warn","warn",-436710552),"Figwheel: code autoloading is OFF");
var inst_37044 = new cljs.core.Keyword(null,"files","files",-472457450).cljs$core$IFn$_invoke$arity$1(inst_37017);
var inst_37045 = cljs.core.map.call(null,new cljs.core.Keyword(null,"file","file",-1269645878),inst_37044);
var inst_37046 = [cljs.core.str("Not loading: "),cljs.core.str(inst_37045)].join('');
var inst_37047 = figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"info","info",-317069002),inst_37046);
var state_37057__$1 = (function (){var statearr_37070 = state_37057;
(statearr_37070[(10)] = inst_37043);

return statearr_37070;
})();
var statearr_37071_37099 = state_37057__$1;
(statearr_37071_37099[(2)] = inst_37047);

(statearr_37071_37099[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (5))){
var inst_37010 = (state_37057[(7)]);
var inst_37012 = [new cljs.core.Keyword(null,"compile-warning","compile-warning",43425356),null,new cljs.core.Keyword(null,"files-changed","files-changed",-1418200563),null];
var inst_37013 = (new cljs.core.PersistentArrayMap(null,2,inst_37012,null));
var inst_37014 = (new cljs.core.PersistentHashSet(null,inst_37013,null));
var inst_37015 = figwheel.client.focus_msgs.call(null,inst_37014,inst_37010);
var inst_37016 = cljs.core.map.call(null,new cljs.core.Keyword(null,"msg-name","msg-name",-353709863),inst_37015);
var inst_37017 = cljs.core.first.call(null,inst_37015);
var inst_37018 = figwheel.client.autoload_QMARK_.call(null);
var state_37057__$1 = (function (){var statearr_37072 = state_37057;
(statearr_37072[(8)] = inst_37017);

(statearr_37072[(9)] = inst_37016);

return statearr_37072;
})();
if(cljs.core.truth_(inst_37018)){
var statearr_37073_37100 = state_37057__$1;
(statearr_37073_37100[(1)] = (8));

} else {
var statearr_37074_37101 = state_37057__$1;
(statearr_37074_37101[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (14))){
var inst_37028 = (state_37057[(2)]);
var state_37057__$1 = state_37057;
var statearr_37075_37102 = state_37057__$1;
(statearr_37075_37102[(2)] = inst_37028);

(statearr_37075_37102[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (16))){
var state_37057__$1 = state_37057;
var statearr_37076_37103 = state_37057__$1;
(statearr_37076_37103[(2)] = null);

(statearr_37076_37103[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (10))){
var inst_37049 = (state_37057[(2)]);
var state_37057__$1 = (function (){var statearr_37077 = state_37057;
(statearr_37077[(11)] = inst_37049);

return statearr_37077;
})();
var statearr_37078_37104 = state_37057__$1;
(statearr_37078_37104[(2)] = null);

(statearr_37078_37104[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37058 === (8))){
var inst_37016 = (state_37057[(9)]);
var inst_37020 = figwheel.client.reload_file_state_QMARK_.call(null,inst_37016,opts);
var state_37057__$1 = state_37057;
if(cljs.core.truth_(inst_37020)){
var statearr_37079_37105 = state_37057__$1;
(statearr_37079_37105[(1)] = (11));

} else {
var statearr_37080_37106 = state_37057__$1;
(statearr_37080_37106[(1)] = (12));

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
});})(c__29520__auto___37088,ch))
;
return ((function (switch__29408__auto__,c__29520__auto___37088,ch){
return (function() {
var figwheel$client$file_reloader_plugin_$_state_machine__29409__auto__ = null;
var figwheel$client$file_reloader_plugin_$_state_machine__29409__auto____0 = (function (){
var statearr_37084 = [null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_37084[(0)] = figwheel$client$file_reloader_plugin_$_state_machine__29409__auto__);

(statearr_37084[(1)] = (1));

return statearr_37084;
});
var figwheel$client$file_reloader_plugin_$_state_machine__29409__auto____1 = (function (state_37057){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_37057);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e37085){if((e37085 instanceof Object)){
var ex__29412__auto__ = e37085;
var statearr_37086_37107 = state_37057;
(statearr_37086_37107[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_37057);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e37085;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37108 = state_37057;
state_37057 = G__37108;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
figwheel$client$file_reloader_plugin_$_state_machine__29409__auto__ = function(state_37057){
switch(arguments.length){
case 0:
return figwheel$client$file_reloader_plugin_$_state_machine__29409__auto____0.call(this);
case 1:
return figwheel$client$file_reloader_plugin_$_state_machine__29409__auto____1.call(this,state_37057);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
figwheel$client$file_reloader_plugin_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = figwheel$client$file_reloader_plugin_$_state_machine__29409__auto____0;
figwheel$client$file_reloader_plugin_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = figwheel$client$file_reloader_plugin_$_state_machine__29409__auto____1;
return figwheel$client$file_reloader_plugin_$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___37088,ch))
})();
var state__29522__auto__ = (function (){var statearr_37087 = f__29521__auto__.call(null);
(statearr_37087[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___37088);

return statearr_37087;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___37088,ch))
);


return ((function (ch){
return (function (msg_hist){
cljs.core.async.put_BANG_.call(null,ch,msg_hist);

return msg_hist;
});
;})(ch))
});
figwheel.client.truncate_stack_trace = (function figwheel$client$truncate_stack_trace(stack_str){
return cljs.core.take_while.call(null,(function (p1__37109_SHARP_){
return cljs.core.not.call(null,cljs.core.re_matches.call(null,/.*eval_javascript_STAR__STAR_.*/,p1__37109_SHARP_));
}),clojure.string.split_lines.call(null,stack_str));
});
figwheel.client.get_ua_product = (function figwheel$client$get_ua_product(){
if(cljs.core.truth_(figwheel.client.utils.node_env_QMARK_.call(null))){
return new cljs.core.Keyword(null,"chrome","chrome",1718738387);
} else {
if(cljs.core.truth_(goog.userAgent.product.SAFARI)){
return new cljs.core.Keyword(null,"safari","safari",497115653);
} else {
if(cljs.core.truth_(goog.userAgent.product.CHROME)){
return new cljs.core.Keyword(null,"chrome","chrome",1718738387);
} else {
if(cljs.core.truth_(goog.userAgent.product.FIREFOX)){
return new cljs.core.Keyword(null,"firefox","firefox",1283768880);
} else {
if(cljs.core.truth_(goog.userAgent.product.IE)){
return new cljs.core.Keyword(null,"ie","ie",2038473780);
} else {
return null;
}
}
}
}
}
});
var base_path_37112 = figwheel.client.utils.base_url_path.call(null);
figwheel.client.eval_javascript_STAR__STAR_ = ((function (base_path_37112){
return (function figwheel$client$eval_javascript_STAR__STAR_(code,opts,result_handler){
try{figwheel.client.enable_repl_print_BANG_.call(null);

var result_value = figwheel.client.utils.eval_helper.call(null,code,opts);
return result_handler.call(null,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"status","status",-1997798413),new cljs.core.Keyword(null,"success","success",1890645906),new cljs.core.Keyword(null,"ua-product","ua-product",938384227),figwheel.client.get_ua_product.call(null),new cljs.core.Keyword(null,"value","value",305978217),result_value], null));
}catch (e37111){if((e37111 instanceof Error)){
var e = e37111;
return result_handler.call(null,new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"status","status",-1997798413),new cljs.core.Keyword(null,"exception","exception",-335277064),new cljs.core.Keyword(null,"value","value",305978217),cljs.core.pr_str.call(null,e),new cljs.core.Keyword(null,"ua-product","ua-product",938384227),figwheel.client.get_ua_product.call(null),new cljs.core.Keyword(null,"stacktrace","stacktrace",-95588394),clojure.string.join.call(null,"\n",figwheel.client.truncate_stack_trace.call(null,e.stack)),new cljs.core.Keyword(null,"base-path","base-path",495760020),base_path_37112], null));
} else {
var e = e37111;
return result_handler.call(null,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"status","status",-1997798413),new cljs.core.Keyword(null,"exception","exception",-335277064),new cljs.core.Keyword(null,"ua-product","ua-product",938384227),figwheel.client.get_ua_product.call(null),new cljs.core.Keyword(null,"value","value",305978217),cljs.core.pr_str.call(null,e),new cljs.core.Keyword(null,"stacktrace","stacktrace",-95588394),"No stacktrace available."], null));

}
}finally {figwheel.client.enable_repl_print_BANG_.call(null);
}});})(base_path_37112))
;
/**
 * The REPL can disconnect and reconnect lets ensure cljs.user exists at least.
 */
figwheel.client.ensure_cljs_user = (function figwheel$client$ensure_cljs_user(){
if(cljs.core.truth_(cljs.user)){
return null;
} else {
return cljs.user = ({});
}
});
figwheel.client.repl_plugin = (function figwheel$client$repl_plugin(p__37113){
var map__37122 = p__37113;
var map__37122__$1 = ((((!((map__37122 == null)))?((((map__37122.cljs$lang$protocol_mask$partition0$ & (64))) || (map__37122.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__37122):map__37122);
var opts = map__37122__$1;
var build_id = cljs.core.get.call(null,map__37122__$1,new cljs.core.Keyword(null,"build-id","build-id",1642831089));
return ((function (map__37122,map__37122__$1,opts,build_id){
return (function (p__37124){
var vec__37125 = p__37124;
var seq__37126 = cljs.core.seq.call(null,vec__37125);
var first__37127 = cljs.core.first.call(null,seq__37126);
var seq__37126__$1 = cljs.core.next.call(null,seq__37126);
var map__37128 = first__37127;
var map__37128__$1 = ((((!((map__37128 == null)))?((((map__37128.cljs$lang$protocol_mask$partition0$ & (64))) || (map__37128.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__37128):map__37128);
var msg = map__37128__$1;
var msg_name = cljs.core.get.call(null,map__37128__$1,new cljs.core.Keyword(null,"msg-name","msg-name",-353709863));
var _ = seq__37126__$1;
if(cljs.core._EQ_.call(null,new cljs.core.Keyword(null,"repl-eval","repl-eval",-1784727398),msg_name)){
figwheel.client.ensure_cljs_user.call(null);

return figwheel.client.eval_javascript_STAR__STAR_.call(null,new cljs.core.Keyword(null,"code","code",1586293142).cljs$core$IFn$_invoke$arity$1(msg),opts,((function (vec__37125,seq__37126,first__37127,seq__37126__$1,map__37128,map__37128__$1,msg,msg_name,_,map__37122,map__37122__$1,opts,build_id){
return (function (res){
return figwheel.client.socket.send_BANG_.call(null,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"figwheel-event","figwheel-event",519570592),"callback",new cljs.core.Keyword(null,"callback-name","callback-name",336964714),new cljs.core.Keyword(null,"callback-name","callback-name",336964714).cljs$core$IFn$_invoke$arity$1(msg),new cljs.core.Keyword(null,"content","content",15833224),res], null));
});})(vec__37125,seq__37126,first__37127,seq__37126__$1,map__37128,map__37128__$1,msg,msg_name,_,map__37122,map__37122__$1,opts,build_id))
);
} else {
return null;
}
});
;})(map__37122,map__37122__$1,opts,build_id))
});
figwheel.client.css_reloader_plugin = (function figwheel$client$css_reloader_plugin(opts){
return (function (p__37136){
var vec__37137 = p__37136;
var seq__37138 = cljs.core.seq.call(null,vec__37137);
var first__37139 = cljs.core.first.call(null,seq__37138);
var seq__37138__$1 = cljs.core.next.call(null,seq__37138);
var map__37140 = first__37139;
var map__37140__$1 = ((((!((map__37140 == null)))?((((map__37140.cljs$lang$protocol_mask$partition0$ & (64))) || (map__37140.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__37140):map__37140);
var msg = map__37140__$1;
var msg_name = cljs.core.get.call(null,map__37140__$1,new cljs.core.Keyword(null,"msg-name","msg-name",-353709863));
var _ = seq__37138__$1;
if(cljs.core._EQ_.call(null,msg_name,new cljs.core.Keyword(null,"css-files-changed","css-files-changed",720773874))){
return figwheel.client.file_reloading.reload_css_files.call(null,opts,msg);
} else {
return null;
}
});
});
figwheel.client.compile_fail_warning_plugin = (function figwheel$client$compile_fail_warning_plugin(p__37142){
var map__37154 = p__37142;
var map__37154__$1 = ((((!((map__37154 == null)))?((((map__37154.cljs$lang$protocol_mask$partition0$ & (64))) || (map__37154.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__37154):map__37154);
var on_compile_warning = cljs.core.get.call(null,map__37154__$1,new cljs.core.Keyword(null,"on-compile-warning","on-compile-warning",-1195585947));
var on_compile_fail = cljs.core.get.call(null,map__37154__$1,new cljs.core.Keyword(null,"on-compile-fail","on-compile-fail",728013036));
return ((function (map__37154,map__37154__$1,on_compile_warning,on_compile_fail){
return (function (p__37156){
var vec__37157 = p__37156;
var seq__37158 = cljs.core.seq.call(null,vec__37157);
var first__37159 = cljs.core.first.call(null,seq__37158);
var seq__37158__$1 = cljs.core.next.call(null,seq__37158);
var map__37160 = first__37159;
var map__37160__$1 = ((((!((map__37160 == null)))?((((map__37160.cljs$lang$protocol_mask$partition0$ & (64))) || (map__37160.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__37160):map__37160);
var msg = map__37160__$1;
var msg_name = cljs.core.get.call(null,map__37160__$1,new cljs.core.Keyword(null,"msg-name","msg-name",-353709863));
var _ = seq__37158__$1;
var pred__37162 = cljs.core._EQ_;
var expr__37163 = msg_name;
if(cljs.core.truth_(pred__37162.call(null,new cljs.core.Keyword(null,"compile-warning","compile-warning",43425356),expr__37163))){
return on_compile_warning.call(null,msg);
} else {
if(cljs.core.truth_(pred__37162.call(null,new cljs.core.Keyword(null,"compile-failed","compile-failed",-477639289),expr__37163))){
return on_compile_fail.call(null,msg);
} else {
return null;
}
}
});
;})(map__37154,map__37154__$1,on_compile_warning,on_compile_fail))
});
figwheel.client.auto_jump_to_error = (function figwheel$client$auto_jump_to_error(opts,error){
if(cljs.core.truth_(new cljs.core.Keyword(null,"auto-jump-to-source-on-error","auto-jump-to-source-on-error",-960314920).cljs$core$IFn$_invoke$arity$1(opts))){
return figwheel.client.heads_up.auto_notify_source_file_line.call(null,error);
} else {
return null;
}
});
figwheel.client.heads_up_plugin_msg_handler = (function figwheel$client$heads_up_plugin_msg_handler(opts,msg_hist_SINGLEQUOTE_){
var msg_hist = figwheel.client.focus_msgs.call(null,new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"compile-failed","compile-failed",-477639289),null,new cljs.core.Keyword(null,"compile-warning","compile-warning",43425356),null,new cljs.core.Keyword(null,"files-changed","files-changed",-1418200563),null], null), null),msg_hist_SINGLEQUOTE_);
var msg_names = cljs.core.map.call(null,new cljs.core.Keyword(null,"msg-name","msg-name",-353709863),msg_hist);
var msg = cljs.core.first.call(null,msg_hist);
var c__29520__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto__,msg_hist,msg_names,msg){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto__,msg_hist,msg_names,msg){
return (function (state_37391){
var state_val_37392 = (state_37391[(1)]);
if((state_val_37392 === (7))){
var inst_37311 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
if(cljs.core.truth_(inst_37311)){
var statearr_37393_37443 = state_37391__$1;
(statearr_37393_37443[(1)] = (8));

} else {
var statearr_37394_37444 = state_37391__$1;
(statearr_37394_37444[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (20))){
var inst_37385 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
var statearr_37395_37445 = state_37391__$1;
(statearr_37395_37445[(2)] = inst_37385);

(statearr_37395_37445[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (27))){
var inst_37381 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
var statearr_37396_37446 = state_37391__$1;
(statearr_37396_37446[(2)] = inst_37381);

(statearr_37396_37446[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (1))){
var inst_37304 = figwheel.client.reload_file_state_QMARK_.call(null,msg_names,opts);
var state_37391__$1 = state_37391;
if(cljs.core.truth_(inst_37304)){
var statearr_37397_37447 = state_37391__$1;
(statearr_37397_37447[(1)] = (2));

} else {
var statearr_37398_37448 = state_37391__$1;
(statearr_37398_37448[(1)] = (3));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (24))){
var inst_37383 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
var statearr_37399_37449 = state_37391__$1;
(statearr_37399_37449[(2)] = inst_37383);

(statearr_37399_37449[(1)] = (20));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (4))){
var inst_37389 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_37391__$1,inst_37389);
} else {
if((state_val_37392 === (15))){
var inst_37387 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
var statearr_37400_37450 = state_37391__$1;
(statearr_37400_37450[(2)] = inst_37387);

(statearr_37400_37450[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (21))){
var inst_37340 = (state_37391[(2)]);
var inst_37341 = new cljs.core.Keyword(null,"exception-data","exception-data",-512474886).cljs$core$IFn$_invoke$arity$1(msg);
var inst_37342 = figwheel.client.auto_jump_to_error.call(null,opts,inst_37341);
var state_37391__$1 = (function (){var statearr_37401 = state_37391;
(statearr_37401[(7)] = inst_37340);

return statearr_37401;
})();
var statearr_37402_37451 = state_37391__$1;
(statearr_37402_37451[(2)] = inst_37342);

(statearr_37402_37451[(1)] = (20));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (31))){
var inst_37370 = figwheel.client.css_loaded_state_QMARK_.call(null,msg_names);
var state_37391__$1 = state_37391;
if(cljs.core.truth_(inst_37370)){
var statearr_37403_37452 = state_37391__$1;
(statearr_37403_37452[(1)] = (34));

} else {
var statearr_37404_37453 = state_37391__$1;
(statearr_37404_37453[(1)] = (35));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (32))){
var inst_37379 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
var statearr_37405_37454 = state_37391__$1;
(statearr_37405_37454[(2)] = inst_37379);

(statearr_37405_37454[(1)] = (27));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (33))){
var inst_37366 = (state_37391[(2)]);
var inst_37367 = new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(msg);
var inst_37368 = figwheel.client.auto_jump_to_error.call(null,opts,inst_37367);
var state_37391__$1 = (function (){var statearr_37406 = state_37391;
(statearr_37406[(8)] = inst_37366);

return statearr_37406;
})();
var statearr_37407_37455 = state_37391__$1;
(statearr_37407_37455[(2)] = inst_37368);

(statearr_37407_37455[(1)] = (32));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (13))){
var inst_37325 = figwheel.client.heads_up.clear.call(null);
var state_37391__$1 = state_37391;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37391__$1,(16),inst_37325);
} else {
if((state_val_37392 === (22))){
var inst_37346 = new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(msg);
var inst_37347 = figwheel.client.heads_up.append_warning_message.call(null,inst_37346);
var state_37391__$1 = state_37391;
var statearr_37408_37456 = state_37391__$1;
(statearr_37408_37456[(2)] = inst_37347);

(statearr_37408_37456[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (36))){
var inst_37377 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
var statearr_37409_37457 = state_37391__$1;
(statearr_37409_37457[(2)] = inst_37377);

(statearr_37409_37457[(1)] = (32));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (29))){
var inst_37357 = (state_37391[(2)]);
var inst_37358 = new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(msg);
var inst_37359 = figwheel.client.auto_jump_to_error.call(null,opts,inst_37358);
var state_37391__$1 = (function (){var statearr_37410 = state_37391;
(statearr_37410[(9)] = inst_37357);

return statearr_37410;
})();
var statearr_37411_37458 = state_37391__$1;
(statearr_37411_37458[(2)] = inst_37359);

(statearr_37411_37458[(1)] = (27));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (6))){
var inst_37306 = (state_37391[(10)]);
var state_37391__$1 = state_37391;
var statearr_37412_37459 = state_37391__$1;
(statearr_37412_37459[(2)] = inst_37306);

(statearr_37412_37459[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (28))){
var inst_37353 = (state_37391[(2)]);
var inst_37354 = new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(msg);
var inst_37355 = figwheel.client.heads_up.display_warning.call(null,inst_37354);
var state_37391__$1 = (function (){var statearr_37413 = state_37391;
(statearr_37413[(11)] = inst_37353);

return statearr_37413;
})();
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37391__$1,(29),inst_37355);
} else {
if((state_val_37392 === (25))){
var inst_37351 = figwheel.client.heads_up.clear.call(null);
var state_37391__$1 = state_37391;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37391__$1,(28),inst_37351);
} else {
if((state_val_37392 === (34))){
var inst_37372 = figwheel.client.heads_up.flash_loaded.call(null);
var state_37391__$1 = state_37391;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37391__$1,(37),inst_37372);
} else {
if((state_val_37392 === (17))){
var inst_37331 = (state_37391[(2)]);
var inst_37332 = new cljs.core.Keyword(null,"exception-data","exception-data",-512474886).cljs$core$IFn$_invoke$arity$1(msg);
var inst_37333 = figwheel.client.auto_jump_to_error.call(null,opts,inst_37332);
var state_37391__$1 = (function (){var statearr_37414 = state_37391;
(statearr_37414[(12)] = inst_37331);

return statearr_37414;
})();
var statearr_37415_37460 = state_37391__$1;
(statearr_37415_37460[(2)] = inst_37333);

(statearr_37415_37460[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (3))){
var inst_37323 = figwheel.client.compile_refail_state_QMARK_.call(null,msg_names);
var state_37391__$1 = state_37391;
if(cljs.core.truth_(inst_37323)){
var statearr_37416_37461 = state_37391__$1;
(statearr_37416_37461[(1)] = (13));

} else {
var statearr_37417_37462 = state_37391__$1;
(statearr_37417_37462[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (12))){
var inst_37319 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
var statearr_37418_37463 = state_37391__$1;
(statearr_37418_37463[(2)] = inst_37319);

(statearr_37418_37463[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (2))){
var inst_37306 = (state_37391[(10)]);
var inst_37306__$1 = figwheel.client.autoload_QMARK_.call(null);
var state_37391__$1 = (function (){var statearr_37419 = state_37391;
(statearr_37419[(10)] = inst_37306__$1);

return statearr_37419;
})();
if(cljs.core.truth_(inst_37306__$1)){
var statearr_37420_37464 = state_37391__$1;
(statearr_37420_37464[(1)] = (5));

} else {
var statearr_37421_37465 = state_37391__$1;
(statearr_37421_37465[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (23))){
var inst_37349 = figwheel.client.rewarning_state_QMARK_.call(null,msg_names);
var state_37391__$1 = state_37391;
if(cljs.core.truth_(inst_37349)){
var statearr_37422_37466 = state_37391__$1;
(statearr_37422_37466[(1)] = (25));

} else {
var statearr_37423_37467 = state_37391__$1;
(statearr_37423_37467[(1)] = (26));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (35))){
var state_37391__$1 = state_37391;
var statearr_37424_37468 = state_37391__$1;
(statearr_37424_37468[(2)] = null);

(statearr_37424_37468[(1)] = (36));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (19))){
var inst_37344 = figwheel.client.warning_append_state_QMARK_.call(null,msg_names);
var state_37391__$1 = state_37391;
if(cljs.core.truth_(inst_37344)){
var statearr_37425_37469 = state_37391__$1;
(statearr_37425_37469[(1)] = (22));

} else {
var statearr_37426_37470 = state_37391__$1;
(statearr_37426_37470[(1)] = (23));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (11))){
var inst_37315 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
var statearr_37427_37471 = state_37391__$1;
(statearr_37427_37471[(2)] = inst_37315);

(statearr_37427_37471[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (9))){
var inst_37317 = figwheel.client.heads_up.clear.call(null);
var state_37391__$1 = state_37391;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37391__$1,(12),inst_37317);
} else {
if((state_val_37392 === (5))){
var inst_37308 = new cljs.core.Keyword(null,"autoload","autoload",-354122500).cljs$core$IFn$_invoke$arity$1(opts);
var state_37391__$1 = state_37391;
var statearr_37428_37472 = state_37391__$1;
(statearr_37428_37472[(2)] = inst_37308);

(statearr_37428_37472[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (14))){
var inst_37335 = figwheel.client.compile_fail_state_QMARK_.call(null,msg_names);
var state_37391__$1 = state_37391;
if(cljs.core.truth_(inst_37335)){
var statearr_37429_37473 = state_37391__$1;
(statearr_37429_37473[(1)] = (18));

} else {
var statearr_37430_37474 = state_37391__$1;
(statearr_37430_37474[(1)] = (19));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (26))){
var inst_37361 = figwheel.client.warning_state_QMARK_.call(null,msg_names);
var state_37391__$1 = state_37391;
if(cljs.core.truth_(inst_37361)){
var statearr_37431_37475 = state_37391__$1;
(statearr_37431_37475[(1)] = (30));

} else {
var statearr_37432_37476 = state_37391__$1;
(statearr_37432_37476[(1)] = (31));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (16))){
var inst_37327 = (state_37391[(2)]);
var inst_37328 = new cljs.core.Keyword(null,"exception-data","exception-data",-512474886).cljs$core$IFn$_invoke$arity$1(msg);
var inst_37329 = figwheel.client.heads_up.display_exception.call(null,inst_37328);
var state_37391__$1 = (function (){var statearr_37433 = state_37391;
(statearr_37433[(13)] = inst_37327);

return statearr_37433;
})();
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37391__$1,(17),inst_37329);
} else {
if((state_val_37392 === (30))){
var inst_37363 = new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(msg);
var inst_37364 = figwheel.client.heads_up.display_warning.call(null,inst_37363);
var state_37391__$1 = state_37391;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37391__$1,(33),inst_37364);
} else {
if((state_val_37392 === (10))){
var inst_37321 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
var statearr_37434_37477 = state_37391__$1;
(statearr_37434_37477[(2)] = inst_37321);

(statearr_37434_37477[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (18))){
var inst_37337 = new cljs.core.Keyword(null,"exception-data","exception-data",-512474886).cljs$core$IFn$_invoke$arity$1(msg);
var inst_37338 = figwheel.client.heads_up.display_exception.call(null,inst_37337);
var state_37391__$1 = state_37391;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37391__$1,(21),inst_37338);
} else {
if((state_val_37392 === (37))){
var inst_37374 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
var statearr_37435_37478 = state_37391__$1;
(statearr_37435_37478[(2)] = inst_37374);

(statearr_37435_37478[(1)] = (36));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37392 === (8))){
var inst_37313 = figwheel.client.heads_up.flash_loaded.call(null);
var state_37391__$1 = state_37391;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37391__$1,(11),inst_37313);
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
});})(c__29520__auto__,msg_hist,msg_names,msg))
;
return ((function (switch__29408__auto__,c__29520__auto__,msg_hist,msg_names,msg){
return (function() {
var figwheel$client$heads_up_plugin_msg_handler_$_state_machine__29409__auto__ = null;
var figwheel$client$heads_up_plugin_msg_handler_$_state_machine__29409__auto____0 = (function (){
var statearr_37439 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_37439[(0)] = figwheel$client$heads_up_plugin_msg_handler_$_state_machine__29409__auto__);

(statearr_37439[(1)] = (1));

return statearr_37439;
});
var figwheel$client$heads_up_plugin_msg_handler_$_state_machine__29409__auto____1 = (function (state_37391){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_37391);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e37440){if((e37440 instanceof Object)){
var ex__29412__auto__ = e37440;
var statearr_37441_37479 = state_37391;
(statearr_37441_37479[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_37391);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e37440;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37480 = state_37391;
state_37391 = G__37480;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
figwheel$client$heads_up_plugin_msg_handler_$_state_machine__29409__auto__ = function(state_37391){
switch(arguments.length){
case 0:
return figwheel$client$heads_up_plugin_msg_handler_$_state_machine__29409__auto____0.call(this);
case 1:
return figwheel$client$heads_up_plugin_msg_handler_$_state_machine__29409__auto____1.call(this,state_37391);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
figwheel$client$heads_up_plugin_msg_handler_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = figwheel$client$heads_up_plugin_msg_handler_$_state_machine__29409__auto____0;
figwheel$client$heads_up_plugin_msg_handler_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = figwheel$client$heads_up_plugin_msg_handler_$_state_machine__29409__auto____1;
return figwheel$client$heads_up_plugin_msg_handler_$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto__,msg_hist,msg_names,msg))
})();
var state__29522__auto__ = (function (){var statearr_37442 = f__29521__auto__.call(null);
(statearr_37442[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto__);

return statearr_37442;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto__,msg_hist,msg_names,msg))
);

return c__29520__auto__;
});
figwheel.client.heads_up_plugin = (function figwheel$client$heads_up_plugin(opts){
var ch = cljs.core.async.chan.call(null);
figwheel.client.heads_up_config_options_STAR__STAR_ = opts;

var c__29520__auto___37543 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___37543,ch){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___37543,ch){
return (function (state_37526){
var state_val_37527 = (state_37526[(1)]);
if((state_val_37527 === (1))){
var state_37526__$1 = state_37526;
var statearr_37528_37544 = state_37526__$1;
(statearr_37528_37544[(2)] = null);

(statearr_37528_37544[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37527 === (2))){
var state_37526__$1 = state_37526;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37526__$1,(4),ch);
} else {
if((state_val_37527 === (3))){
var inst_37524 = (state_37526[(2)]);
var state_37526__$1 = state_37526;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_37526__$1,inst_37524);
} else {
if((state_val_37527 === (4))){
var inst_37514 = (state_37526[(7)]);
var inst_37514__$1 = (state_37526[(2)]);
var state_37526__$1 = (function (){var statearr_37529 = state_37526;
(statearr_37529[(7)] = inst_37514__$1);

return statearr_37529;
})();
if(cljs.core.truth_(inst_37514__$1)){
var statearr_37530_37545 = state_37526__$1;
(statearr_37530_37545[(1)] = (5));

} else {
var statearr_37531_37546 = state_37526__$1;
(statearr_37531_37546[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37527 === (5))){
var inst_37514 = (state_37526[(7)]);
var inst_37516 = figwheel.client.heads_up_plugin_msg_handler.call(null,opts,inst_37514);
var state_37526__$1 = state_37526;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37526__$1,(8),inst_37516);
} else {
if((state_val_37527 === (6))){
var state_37526__$1 = state_37526;
var statearr_37532_37547 = state_37526__$1;
(statearr_37532_37547[(2)] = null);

(statearr_37532_37547[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37527 === (7))){
var inst_37522 = (state_37526[(2)]);
var state_37526__$1 = state_37526;
var statearr_37533_37548 = state_37526__$1;
(statearr_37533_37548[(2)] = inst_37522);

(statearr_37533_37548[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37527 === (8))){
var inst_37518 = (state_37526[(2)]);
var state_37526__$1 = (function (){var statearr_37534 = state_37526;
(statearr_37534[(8)] = inst_37518);

return statearr_37534;
})();
var statearr_37535_37549 = state_37526__$1;
(statearr_37535_37549[(2)] = null);

(statearr_37535_37549[(1)] = (2));


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
});})(c__29520__auto___37543,ch))
;
return ((function (switch__29408__auto__,c__29520__auto___37543,ch){
return (function() {
var figwheel$client$heads_up_plugin_$_state_machine__29409__auto__ = null;
var figwheel$client$heads_up_plugin_$_state_machine__29409__auto____0 = (function (){
var statearr_37539 = [null,null,null,null,null,null,null,null,null];
(statearr_37539[(0)] = figwheel$client$heads_up_plugin_$_state_machine__29409__auto__);

(statearr_37539[(1)] = (1));

return statearr_37539;
});
var figwheel$client$heads_up_plugin_$_state_machine__29409__auto____1 = (function (state_37526){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_37526);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e37540){if((e37540 instanceof Object)){
var ex__29412__auto__ = e37540;
var statearr_37541_37550 = state_37526;
(statearr_37541_37550[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_37526);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e37540;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37551 = state_37526;
state_37526 = G__37551;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
figwheel$client$heads_up_plugin_$_state_machine__29409__auto__ = function(state_37526){
switch(arguments.length){
case 0:
return figwheel$client$heads_up_plugin_$_state_machine__29409__auto____0.call(this);
case 1:
return figwheel$client$heads_up_plugin_$_state_machine__29409__auto____1.call(this,state_37526);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
figwheel$client$heads_up_plugin_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = figwheel$client$heads_up_plugin_$_state_machine__29409__auto____0;
figwheel$client$heads_up_plugin_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = figwheel$client$heads_up_plugin_$_state_machine__29409__auto____1;
return figwheel$client$heads_up_plugin_$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___37543,ch))
})();
var state__29522__auto__ = (function (){var statearr_37542 = f__29521__auto__.call(null);
(statearr_37542[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___37543);

return statearr_37542;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___37543,ch))
);


figwheel.client.heads_up.ensure_container.call(null);

return ((function (ch){
return (function (msg_hist){
cljs.core.async.put_BANG_.call(null,ch,msg_hist);

return msg_hist;
});
;})(ch))
});
figwheel.client.enforce_project_plugin = (function figwheel$client$enforce_project_plugin(opts){
return (function (msg_hist){
if(((1) < cljs.core.count.call(null,cljs.core.set.call(null,cljs.core.keep.call(null,new cljs.core.Keyword(null,"project-id","project-id",206449307),cljs.core.take.call(null,(5),msg_hist)))))){
figwheel.client.socket.close_BANG_.call(null);

console.error("Figwheel: message received from different project. Shutting socket down.");

if(cljs.core.truth_(new cljs.core.Keyword(null,"heads-up-display","heads-up-display",-896577202).cljs$core$IFn$_invoke$arity$1(opts))){
var c__29520__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto__){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto__){
return (function (state_37572){
var state_val_37573 = (state_37572[(1)]);
if((state_val_37573 === (1))){
var inst_37567 = cljs.core.async.timeout.call(null,(3000));
var state_37572__$1 = state_37572;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37572__$1,(2),inst_37567);
} else {
if((state_val_37573 === (2))){
var inst_37569 = (state_37572[(2)]);
var inst_37570 = figwheel.client.heads_up.display_system_warning.call(null,"Connection from different project","Shutting connection down!!!!!");
var state_37572__$1 = (function (){var statearr_37574 = state_37572;
(statearr_37574[(7)] = inst_37569);

return statearr_37574;
})();
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_37572__$1,inst_37570);
} else {
return null;
}
}
});})(c__29520__auto__))
;
return ((function (switch__29408__auto__,c__29520__auto__){
return (function() {
var figwheel$client$enforce_project_plugin_$_state_machine__29409__auto__ = null;
var figwheel$client$enforce_project_plugin_$_state_machine__29409__auto____0 = (function (){
var statearr_37578 = [null,null,null,null,null,null,null,null];
(statearr_37578[(0)] = figwheel$client$enforce_project_plugin_$_state_machine__29409__auto__);

(statearr_37578[(1)] = (1));

return statearr_37578;
});
var figwheel$client$enforce_project_plugin_$_state_machine__29409__auto____1 = (function (state_37572){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_37572);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e37579){if((e37579 instanceof Object)){
var ex__29412__auto__ = e37579;
var statearr_37580_37582 = state_37572;
(statearr_37580_37582[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_37572);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e37579;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37583 = state_37572;
state_37572 = G__37583;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
figwheel$client$enforce_project_plugin_$_state_machine__29409__auto__ = function(state_37572){
switch(arguments.length){
case 0:
return figwheel$client$enforce_project_plugin_$_state_machine__29409__auto____0.call(this);
case 1:
return figwheel$client$enforce_project_plugin_$_state_machine__29409__auto____1.call(this,state_37572);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
figwheel$client$enforce_project_plugin_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = figwheel$client$enforce_project_plugin_$_state_machine__29409__auto____0;
figwheel$client$enforce_project_plugin_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = figwheel$client$enforce_project_plugin_$_state_machine__29409__auto____1;
return figwheel$client$enforce_project_plugin_$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto__))
})();
var state__29522__auto__ = (function (){var statearr_37581 = f__29521__auto__.call(null);
(statearr_37581[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto__);

return statearr_37581;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto__))
);

return c__29520__auto__;
} else {
return null;
}
} else {
return null;
}
});
});
figwheel.client.enforce_figwheel_version_plugin = (function figwheel$client$enforce_figwheel_version_plugin(opts){
return (function (msg_hist){
var temp__4657__auto__ = new cljs.core.Keyword(null,"figwheel-version","figwheel-version",1409553832).cljs$core$IFn$_invoke$arity$1(cljs.core.first.call(null,msg_hist));
if(cljs.core.truth_(temp__4657__auto__)){
var figwheel_version = temp__4657__auto__;
if(cljs.core.not_EQ_.call(null,figwheel_version,figwheel.client._figwheel_version_)){
figwheel.client.socket.close_BANG_.call(null);

console.error("Figwheel: message received from different version of Figwheel.");

if(cljs.core.truth_(new cljs.core.Keyword(null,"heads-up-display","heads-up-display",-896577202).cljs$core$IFn$_invoke$arity$1(opts))){
var c__29520__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto__,figwheel_version,temp__4657__auto__){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto__,figwheel_version,temp__4657__auto__){
return (function (state_37606){
var state_val_37607 = (state_37606[(1)]);
if((state_val_37607 === (1))){
var inst_37600 = cljs.core.async.timeout.call(null,(2000));
var state_37606__$1 = state_37606;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_37606__$1,(2),inst_37600);
} else {
if((state_val_37607 === (2))){
var inst_37602 = (state_37606[(2)]);
var inst_37603 = [cljs.core.str("Figwheel Client Version <strong>"),cljs.core.str(figwheel.client._figwheel_version_),cljs.core.str("</strong> is not equal to "),cljs.core.str("Figwheel Sidecar Version <strong>"),cljs.core.str(figwheel_version),cljs.core.str("</strong>"),cljs.core.str(".  Shutting down Websocket Connection!"),cljs.core.str("<h4>To fix try:</h4>"),cljs.core.str("<ol><li>Reload this page and make sure you are not getting a cached version of the client.</li>"),cljs.core.str("<li>You may have to clean (delete compiled assets) and rebuild to make sure that the new client code is being used.</li>"),cljs.core.str("<li>Also, make sure you have consistent Figwheel dependencies.</li></ol>")].join('');
var inst_37604 = figwheel.client.heads_up.display_system_warning.call(null,"Figwheel Client and Server have different versions!!",inst_37603);
var state_37606__$1 = (function (){var statearr_37608 = state_37606;
(statearr_37608[(7)] = inst_37602);

return statearr_37608;
})();
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_37606__$1,inst_37604);
} else {
return null;
}
}
});})(c__29520__auto__,figwheel_version,temp__4657__auto__))
;
return ((function (switch__29408__auto__,c__29520__auto__,figwheel_version,temp__4657__auto__){
return (function() {
var figwheel$client$enforce_figwheel_version_plugin_$_state_machine__29409__auto__ = null;
var figwheel$client$enforce_figwheel_version_plugin_$_state_machine__29409__auto____0 = (function (){
var statearr_37612 = [null,null,null,null,null,null,null,null];
(statearr_37612[(0)] = figwheel$client$enforce_figwheel_version_plugin_$_state_machine__29409__auto__);

(statearr_37612[(1)] = (1));

return statearr_37612;
});
var figwheel$client$enforce_figwheel_version_plugin_$_state_machine__29409__auto____1 = (function (state_37606){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_37606);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e37613){if((e37613 instanceof Object)){
var ex__29412__auto__ = e37613;
var statearr_37614_37616 = state_37606;
(statearr_37614_37616[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_37606);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e37613;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37617 = state_37606;
state_37606 = G__37617;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
figwheel$client$enforce_figwheel_version_plugin_$_state_machine__29409__auto__ = function(state_37606){
switch(arguments.length){
case 0:
return figwheel$client$enforce_figwheel_version_plugin_$_state_machine__29409__auto____0.call(this);
case 1:
return figwheel$client$enforce_figwheel_version_plugin_$_state_machine__29409__auto____1.call(this,state_37606);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
figwheel$client$enforce_figwheel_version_plugin_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = figwheel$client$enforce_figwheel_version_plugin_$_state_machine__29409__auto____0;
figwheel$client$enforce_figwheel_version_plugin_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = figwheel$client$enforce_figwheel_version_plugin_$_state_machine__29409__auto____1;
return figwheel$client$enforce_figwheel_version_plugin_$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto__,figwheel_version,temp__4657__auto__))
})();
var state__29522__auto__ = (function (){var statearr_37615 = f__29521__auto__.call(null);
(statearr_37615[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto__);

return statearr_37615;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto__,figwheel_version,temp__4657__auto__))
);

return c__29520__auto__;
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
});
});
figwheel.client.default_on_jsload = cljs.core.identity;
figwheel.client.file_line_column = (function figwheel$client$file_line_column(p__37618){
var map__37622 = p__37618;
var map__37622__$1 = ((((!((map__37622 == null)))?((((map__37622.cljs$lang$protocol_mask$partition0$ & (64))) || (map__37622.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__37622):map__37622);
var file = cljs.core.get.call(null,map__37622__$1,new cljs.core.Keyword(null,"file","file",-1269645878));
var line = cljs.core.get.call(null,map__37622__$1,new cljs.core.Keyword(null,"line","line",212345235));
var column = cljs.core.get.call(null,map__37622__$1,new cljs.core.Keyword(null,"column","column",2078222095));
var G__37624 = "";
var G__37624__$1 = (cljs.core.truth_(file)?[cljs.core.str(G__37624),cljs.core.str("file "),cljs.core.str(file)].join(''):G__37624);
var G__37624__$2 = (cljs.core.truth_(line)?[cljs.core.str(G__37624__$1),cljs.core.str(" at line "),cljs.core.str(line)].join(''):G__37624__$1);
if(cljs.core.truth_((function (){var and__24778__auto__ = line;
if(cljs.core.truth_(and__24778__auto__)){
return column;
} else {
return and__24778__auto__;
}
})())){
return [cljs.core.str(G__37624__$2),cljs.core.str(", column "),cljs.core.str(column)].join('');
} else {
return G__37624__$2;
}
});
figwheel.client.default_on_compile_fail = (function figwheel$client$default_on_compile_fail(p__37625){
var map__37632 = p__37625;
var map__37632__$1 = ((((!((map__37632 == null)))?((((map__37632.cljs$lang$protocol_mask$partition0$ & (64))) || (map__37632.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__37632):map__37632);
var ed = map__37632__$1;
var formatted_exception = cljs.core.get.call(null,map__37632__$1,new cljs.core.Keyword(null,"formatted-exception","formatted-exception",-116489026));
var exception_data = cljs.core.get.call(null,map__37632__$1,new cljs.core.Keyword(null,"exception-data","exception-data",-512474886));
var cause = cljs.core.get.call(null,map__37632__$1,new cljs.core.Keyword(null,"cause","cause",231901252));
figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"debug","debug",-1608172596),"Figwheel: Compile Exception");

var seq__37634_37638 = cljs.core.seq.call(null,figwheel.client.format_messages.call(null,exception_data));
var chunk__37635_37639 = null;
var count__37636_37640 = (0);
var i__37637_37641 = (0);
while(true){
if((i__37637_37641 < count__37636_37640)){
var msg_37642 = cljs.core._nth.call(null,chunk__37635_37639,i__37637_37641);
figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"info","info",-317069002),msg_37642);

var G__37643 = seq__37634_37638;
var G__37644 = chunk__37635_37639;
var G__37645 = count__37636_37640;
var G__37646 = (i__37637_37641 + (1));
seq__37634_37638 = G__37643;
chunk__37635_37639 = G__37644;
count__37636_37640 = G__37645;
i__37637_37641 = G__37646;
continue;
} else {
var temp__4657__auto___37647 = cljs.core.seq.call(null,seq__37634_37638);
if(temp__4657__auto___37647){
var seq__37634_37648__$1 = temp__4657__auto___37647;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__37634_37648__$1)){
var c__25601__auto___37649 = cljs.core.chunk_first.call(null,seq__37634_37648__$1);
var G__37650 = cljs.core.chunk_rest.call(null,seq__37634_37648__$1);
var G__37651 = c__25601__auto___37649;
var G__37652 = cljs.core.count.call(null,c__25601__auto___37649);
var G__37653 = (0);
seq__37634_37638 = G__37650;
chunk__37635_37639 = G__37651;
count__37636_37640 = G__37652;
i__37637_37641 = G__37653;
continue;
} else {
var msg_37654 = cljs.core.first.call(null,seq__37634_37648__$1);
figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"info","info",-317069002),msg_37654);

var G__37655 = cljs.core.next.call(null,seq__37634_37648__$1);
var G__37656 = null;
var G__37657 = (0);
var G__37658 = (0);
seq__37634_37638 = G__37655;
chunk__37635_37639 = G__37656;
count__37636_37640 = G__37657;
i__37637_37641 = G__37658;
continue;
}
} else {
}
}
break;
}

if(cljs.core.truth_(cause)){
figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"info","info",-317069002),[cljs.core.str("Error on "),cljs.core.str(figwheel.client.file_line_column.call(null,ed))].join(''));
} else {
}

return ed;
});
figwheel.client.default_on_compile_warning = (function figwheel$client$default_on_compile_warning(p__37659){
var map__37662 = p__37659;
var map__37662__$1 = ((((!((map__37662 == null)))?((((map__37662.cljs$lang$protocol_mask$partition0$ & (64))) || (map__37662.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__37662):map__37662);
var w = map__37662__$1;
var message = cljs.core.get.call(null,map__37662__$1,new cljs.core.Keyword(null,"message","message",-406056002));
figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"warn","warn",-436710552),[cljs.core.str("Figwheel: Compile Warning - "),cljs.core.str(new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(message)),cljs.core.str(" in "),cljs.core.str(figwheel.client.file_line_column.call(null,message))].join(''));

return w;
});
figwheel.client.default_before_load = (function figwheel$client$default_before_load(files){
figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"debug","debug",-1608172596),"Figwheel: notified of file changes");

return files;
});
figwheel.client.default_on_cssload = (function figwheel$client$default_on_cssload(files){
figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"debug","debug",-1608172596),"Figwheel: loaded CSS files");

figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"info","info",-317069002),cljs.core.pr_str.call(null,cljs.core.map.call(null,new cljs.core.Keyword(null,"file","file",-1269645878),files)));

return files;
});
if(typeof figwheel.client.config_defaults !== 'undefined'){
} else {
figwheel.client.config_defaults = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"on-compile-warning","on-compile-warning",-1195585947),new cljs.core.Keyword(null,"on-jsload","on-jsload",-395756602),new cljs.core.Keyword(null,"reload-dependents","reload-dependents",-956865430),new cljs.core.Keyword(null,"on-compile-fail","on-compile-fail",728013036),new cljs.core.Keyword(null,"debug","debug",-1608172596),new cljs.core.Keyword(null,"heads-up-display","heads-up-display",-896577202),new cljs.core.Keyword(null,"websocket-url","websocket-url",-490444938),new cljs.core.Keyword(null,"auto-jump-to-source-on-error","auto-jump-to-source-on-error",-960314920),new cljs.core.Keyword(null,"before-jsload","before-jsload",-847513128),new cljs.core.Keyword(null,"load-warninged-code","load-warninged-code",-2030345223),new cljs.core.Keyword(null,"eval-fn","eval-fn",-1111644294),new cljs.core.Keyword(null,"retry-count","retry-count",1936122875),new cljs.core.Keyword(null,"autoload","autoload",-354122500),new cljs.core.Keyword(null,"on-cssload","on-cssload",1825432318)],[new cljs.core.Var(function(){return figwheel.client.default_on_compile_warning;},new cljs.core.Symbol("figwheel.client","default-on-compile-warning","figwheel.client/default-on-compile-warning",584144208,null),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"ns","ns",441598760),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"file","file",-1269645878),new cljs.core.Keyword(null,"end-column","end-column",1425389514),new cljs.core.Keyword(null,"column","column",2078222095),new cljs.core.Keyword(null,"line","line",212345235),new cljs.core.Keyword(null,"end-line","end-line",1837326455),new cljs.core.Keyword(null,"arglists","arglists",1661989754),new cljs.core.Keyword(null,"doc","doc",1913296891),new cljs.core.Keyword(null,"test","test",577538877)],[new cljs.core.Symbol(null,"figwheel.client","figwheel.client",-538710252,null),new cljs.core.Symbol(null,"default-on-compile-warning","default-on-compile-warning",-18911586,null),"resources/public/js/compiled/devcards_out/figwheel/client.cljs",33,1,336,336,cljs.core.list(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"keys","keys",1068423698),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"message","message",1234475525,null)], null),new cljs.core.Keyword(null,"as","as",1148689641),new cljs.core.Symbol(null,"w","w",1994700528,null)], null)], null)),null,(cljs.core.truth_(figwheel.client.default_on_compile_warning)?figwheel.client.default_on_compile_warning.cljs$lang$test:null)])),figwheel.client.default_on_jsload,true,new cljs.core.Var(function(){return figwheel.client.default_on_compile_fail;},new cljs.core.Symbol("figwheel.client","default-on-compile-fail","figwheel.client/default-on-compile-fail",1384826337,null),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"ns","ns",441598760),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"file","file",-1269645878),new cljs.core.Keyword(null,"end-column","end-column",1425389514),new cljs.core.Keyword(null,"column","column",2078222095),new cljs.core.Keyword(null,"line","line",212345235),new cljs.core.Keyword(null,"end-line","end-line",1837326455),new cljs.core.Keyword(null,"arglists","arglists",1661989754),new cljs.core.Keyword(null,"doc","doc",1913296891),new cljs.core.Keyword(null,"test","test",577538877)],[new cljs.core.Symbol(null,"figwheel.client","figwheel.client",-538710252,null),new cljs.core.Symbol(null,"default-on-compile-fail","default-on-compile-fail",-158814813,null),"resources/public/js/compiled/devcards_out/figwheel/client.cljs",30,1,328,328,cljs.core.list(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"keys","keys",1068423698),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"formatted-exception","formatted-exception",1524042501,null),new cljs.core.Symbol(null,"exception-data","exception-data",1128056641,null),new cljs.core.Symbol(null,"cause","cause",1872432779,null)], null),new cljs.core.Keyword(null,"as","as",1148689641),new cljs.core.Symbol(null,"ed","ed",2076825751,null)], null)], null)),null,(cljs.core.truth_(figwheel.client.default_on_compile_fail)?figwheel.client.default_on_compile_fail.cljs$lang$test:null)])),false,true,[cljs.core.str("ws://"),cljs.core.str((cljs.core.truth_(figwheel.client.utils.html_env_QMARK_.call(null))?location.host:"localhost:3449")),cljs.core.str("/figwheel-ws")].join(''),false,figwheel.client.default_before_load,false,false,(100),true,figwheel.client.default_on_cssload]);
}
figwheel.client.handle_deprecated_jsload_callback = (function figwheel$client$handle_deprecated_jsload_callback(config){
if(cljs.core.truth_(new cljs.core.Keyword(null,"jsload-callback","jsload-callback",-1949628369).cljs$core$IFn$_invoke$arity$1(config))){
return cljs.core.dissoc.call(null,cljs.core.assoc.call(null,config,new cljs.core.Keyword(null,"on-jsload","on-jsload",-395756602),new cljs.core.Keyword(null,"jsload-callback","jsload-callback",-1949628369).cljs$core$IFn$_invoke$arity$1(config)),new cljs.core.Keyword(null,"jsload-callback","jsload-callback",-1949628369));
} else {
return config;
}
});
figwheel.client.fill_url_template = (function figwheel$client$fill_url_template(config){
if(cljs.core.truth_(figwheel.client.utils.html_env_QMARK_.call(null))){
return cljs.core.update_in.call(null,config,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"websocket-url","websocket-url",-490444938)], null),(function (x){
return clojure.string.replace.call(null,clojure.string.replace.call(null,x,"[[client-hostname]]",location.hostname),"[[client-port]]",location.port);
}));
} else {
return config;
}
});
figwheel.client.base_plugins = (function figwheel$client$base_plugins(system_options){
var base = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"enforce-project-plugin","enforce-project-plugin",959402899),figwheel.client.enforce_project_plugin,new cljs.core.Keyword(null,"enforce-figwheel-version-plugin","enforce-figwheel-version-plugin",-1916185220),figwheel.client.enforce_figwheel_version_plugin,new cljs.core.Keyword(null,"file-reloader-plugin","file-reloader-plugin",-1792964733),figwheel.client.file_reloader_plugin,new cljs.core.Keyword(null,"comp-fail-warning-plugin","comp-fail-warning-plugin",634311),figwheel.client.compile_fail_warning_plugin,new cljs.core.Keyword(null,"css-reloader-plugin","css-reloader-plugin",2002032904),figwheel.client.css_reloader_plugin,new cljs.core.Keyword(null,"repl-plugin","repl-plugin",-1138952371),figwheel.client.repl_plugin], null);
var base__$1 = ((cljs.core.not.call(null,figwheel.client.utils.html_env_QMARK_.call(null)))?cljs.core.select_keys.call(null,base,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"file-reloader-plugin","file-reloader-plugin",-1792964733),new cljs.core.Keyword(null,"comp-fail-warning-plugin","comp-fail-warning-plugin",634311),new cljs.core.Keyword(null,"repl-plugin","repl-plugin",-1138952371)], null)):base);
var base__$2 = ((new cljs.core.Keyword(null,"autoload","autoload",-354122500).cljs$core$IFn$_invoke$arity$1(system_options) === false)?cljs.core.dissoc.call(null,base__$1,new cljs.core.Keyword(null,"file-reloader-plugin","file-reloader-plugin",-1792964733)):base__$1);
if(cljs.core.truth_((function (){var and__24778__auto__ = new cljs.core.Keyword(null,"heads-up-display","heads-up-display",-896577202).cljs$core$IFn$_invoke$arity$1(system_options);
if(cljs.core.truth_(and__24778__auto__)){
return figwheel.client.utils.html_env_QMARK_.call(null);
} else {
return and__24778__auto__;
}
})())){
return cljs.core.assoc.call(null,base__$2,new cljs.core.Keyword(null,"heads-up-display-plugin","heads-up-display-plugin",1745207501),figwheel.client.heads_up_plugin);
} else {
return base__$2;
}
});
figwheel.client.add_message_watch = (function figwheel$client$add_message_watch(key,callback){
return cljs.core.add_watch.call(null,figwheel.client.socket.message_history_atom,key,(function (_,___$1,___$2,msg_hist){
return callback.call(null,cljs.core.first.call(null,msg_hist));
}));
});
figwheel.client.add_plugins = (function figwheel$client$add_plugins(plugins,system_options){
var seq__37674 = cljs.core.seq.call(null,plugins);
var chunk__37675 = null;
var count__37676 = (0);
var i__37677 = (0);
while(true){
if((i__37677 < count__37676)){
var vec__37678 = cljs.core._nth.call(null,chunk__37675,i__37677);
var k = cljs.core.nth.call(null,vec__37678,(0),null);
var plugin = cljs.core.nth.call(null,vec__37678,(1),null);
if(cljs.core.truth_(plugin)){
var pl_37684 = plugin.call(null,system_options);
cljs.core.add_watch.call(null,figwheel.client.socket.message_history_atom,k,((function (seq__37674,chunk__37675,count__37676,i__37677,pl_37684,vec__37678,k,plugin){
return (function (_,___$1,___$2,msg_hist){
return pl_37684.call(null,msg_hist);
});})(seq__37674,chunk__37675,count__37676,i__37677,pl_37684,vec__37678,k,plugin))
);
} else {
}

var G__37685 = seq__37674;
var G__37686 = chunk__37675;
var G__37687 = count__37676;
var G__37688 = (i__37677 + (1));
seq__37674 = G__37685;
chunk__37675 = G__37686;
count__37676 = G__37687;
i__37677 = G__37688;
continue;
} else {
var temp__4657__auto__ = cljs.core.seq.call(null,seq__37674);
if(temp__4657__auto__){
var seq__37674__$1 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__37674__$1)){
var c__25601__auto__ = cljs.core.chunk_first.call(null,seq__37674__$1);
var G__37689 = cljs.core.chunk_rest.call(null,seq__37674__$1);
var G__37690 = c__25601__auto__;
var G__37691 = cljs.core.count.call(null,c__25601__auto__);
var G__37692 = (0);
seq__37674 = G__37689;
chunk__37675 = G__37690;
count__37676 = G__37691;
i__37677 = G__37692;
continue;
} else {
var vec__37681 = cljs.core.first.call(null,seq__37674__$1);
var k = cljs.core.nth.call(null,vec__37681,(0),null);
var plugin = cljs.core.nth.call(null,vec__37681,(1),null);
if(cljs.core.truth_(plugin)){
var pl_37693 = plugin.call(null,system_options);
cljs.core.add_watch.call(null,figwheel.client.socket.message_history_atom,k,((function (seq__37674,chunk__37675,count__37676,i__37677,pl_37693,vec__37681,k,plugin,seq__37674__$1,temp__4657__auto__){
return (function (_,___$1,___$2,msg_hist){
return pl_37693.call(null,msg_hist);
});})(seq__37674,chunk__37675,count__37676,i__37677,pl_37693,vec__37681,k,plugin,seq__37674__$1,temp__4657__auto__))
);
} else {
}

var G__37694 = cljs.core.next.call(null,seq__37674__$1);
var G__37695 = null;
var G__37696 = (0);
var G__37697 = (0);
seq__37674 = G__37694;
chunk__37675 = G__37695;
count__37676 = G__37696;
i__37677 = G__37697;
continue;
}
} else {
return null;
}
}
break;
}
});
figwheel.client.start = (function figwheel$client$start(var_args){
var args37698 = [];
var len__25865__auto___37705 = arguments.length;
var i__25866__auto___37706 = (0);
while(true){
if((i__25866__auto___37706 < len__25865__auto___37705)){
args37698.push((arguments[i__25866__auto___37706]));

var G__37707 = (i__25866__auto___37706 + (1));
i__25866__auto___37706 = G__37707;
continue;
} else {
}
break;
}

var G__37700 = args37698.length;
switch (G__37700) {
case 1:
return figwheel.client.start.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 0:
return figwheel.client.start.cljs$core$IFn$_invoke$arity$0();

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args37698.length)].join('')));

}
});

figwheel.client.start.cljs$core$IFn$_invoke$arity$1 = (function (opts){
if((goog.dependencies_ == null)){
return null;
} else {
if(typeof figwheel.client.__figwheel_start_once__ !== 'undefined'){
return null;
} else {
figwheel.client.__figwheel_start_once__ = setTimeout((function (){
var plugins_SINGLEQUOTE_ = new cljs.core.Keyword(null,"plugins","plugins",1900073717).cljs$core$IFn$_invoke$arity$1(opts);
var merge_plugins = new cljs.core.Keyword(null,"merge-plugins","merge-plugins",-1193912370).cljs$core$IFn$_invoke$arity$1(opts);
var system_options = figwheel.client.fill_url_template.call(null,figwheel.client.handle_deprecated_jsload_callback.call(null,cljs.core.merge.call(null,figwheel.client.config_defaults,cljs.core.dissoc.call(null,opts,new cljs.core.Keyword(null,"plugins","plugins",1900073717),new cljs.core.Keyword(null,"merge-plugins","merge-plugins",-1193912370)))));
var plugins = (cljs.core.truth_(plugins_SINGLEQUOTE_)?plugins_SINGLEQUOTE_:cljs.core.merge.call(null,figwheel.client.base_plugins.call(null,system_options),merge_plugins));
figwheel.client.utils._STAR_print_debug_STAR_ = new cljs.core.Keyword(null,"debug","debug",-1608172596).cljs$core$IFn$_invoke$arity$1(opts);

figwheel.client.enable_repl_print_BANG_.call(null);

figwheel.client.add_plugins.call(null,plugins,system_options);

figwheel.client.file_reloading.patch_goog_base.call(null);

var seq__37701_37709 = cljs.core.seq.call(null,new cljs.core.Keyword(null,"initial-messages","initial-messages",2057377771).cljs$core$IFn$_invoke$arity$1(system_options));
var chunk__37702_37710 = null;
var count__37703_37711 = (0);
var i__37704_37712 = (0);
while(true){
if((i__37704_37712 < count__37703_37711)){
var msg_37713 = cljs.core._nth.call(null,chunk__37702_37710,i__37704_37712);
figwheel.client.socket.handle_incoming_message.call(null,msg_37713);

var G__37714 = seq__37701_37709;
var G__37715 = chunk__37702_37710;
var G__37716 = count__37703_37711;
var G__37717 = (i__37704_37712 + (1));
seq__37701_37709 = G__37714;
chunk__37702_37710 = G__37715;
count__37703_37711 = G__37716;
i__37704_37712 = G__37717;
continue;
} else {
var temp__4657__auto___37718 = cljs.core.seq.call(null,seq__37701_37709);
if(temp__4657__auto___37718){
var seq__37701_37719__$1 = temp__4657__auto___37718;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__37701_37719__$1)){
var c__25601__auto___37720 = cljs.core.chunk_first.call(null,seq__37701_37719__$1);
var G__37721 = cljs.core.chunk_rest.call(null,seq__37701_37719__$1);
var G__37722 = c__25601__auto___37720;
var G__37723 = cljs.core.count.call(null,c__25601__auto___37720);
var G__37724 = (0);
seq__37701_37709 = G__37721;
chunk__37702_37710 = G__37722;
count__37703_37711 = G__37723;
i__37704_37712 = G__37724;
continue;
} else {
var msg_37725 = cljs.core.first.call(null,seq__37701_37719__$1);
figwheel.client.socket.handle_incoming_message.call(null,msg_37725);

var G__37726 = cljs.core.next.call(null,seq__37701_37719__$1);
var G__37727 = null;
var G__37728 = (0);
var G__37729 = (0);
seq__37701_37709 = G__37726;
chunk__37702_37710 = G__37727;
count__37703_37711 = G__37728;
i__37704_37712 = G__37729;
continue;
}
} else {
}
}
break;
}

return figwheel.client.socket.open.call(null,system_options);
}));
}
}
});

figwheel.client.start.cljs$core$IFn$_invoke$arity$0 = (function (){
return figwheel.client.start.call(null,cljs.core.PersistentArrayMap.EMPTY);
});

figwheel.client.start.cljs$lang$maxFixedArity = 1;

figwheel.client.watch_and_reload_with_opts = figwheel.client.start;
figwheel.client.watch_and_reload = (function figwheel$client$watch_and_reload(var_args){
var args__25872__auto__ = [];
var len__25865__auto___37734 = arguments.length;
var i__25866__auto___37735 = (0);
while(true){
if((i__25866__auto___37735 < len__25865__auto___37734)){
args__25872__auto__.push((arguments[i__25866__auto___37735]));

var G__37736 = (i__25866__auto___37735 + (1));
i__25866__auto___37735 = G__37736;
continue;
} else {
}
break;
}

var argseq__25873__auto__ = ((((0) < args__25872__auto__.length))?(new cljs.core.IndexedSeq(args__25872__auto__.slice((0)),(0),null)):null);
return figwheel.client.watch_and_reload.cljs$core$IFn$_invoke$arity$variadic(argseq__25873__auto__);
});

figwheel.client.watch_and_reload.cljs$core$IFn$_invoke$arity$variadic = (function (p__37731){
var map__37732 = p__37731;
var map__37732__$1 = ((((!((map__37732 == null)))?((((map__37732.cljs$lang$protocol_mask$partition0$ & (64))) || (map__37732.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__37732):map__37732);
var opts = map__37732__$1;
return figwheel.client.start.call(null,opts);
});

figwheel.client.watch_and_reload.cljs$lang$maxFixedArity = (0);

figwheel.client.watch_and_reload.cljs$lang$applyTo = (function (seq37730){
return figwheel.client.watch_and_reload.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq.call(null,seq37730));
});

figwheel.client.fetch_data_from_env = (function figwheel$client$fetch_data_from_env(){
try{return cljs.reader.read_string.call(null,goog.object.get(window,"FIGWHEEL_CLIENT_CONFIGURATION"));
}catch (e37738){if((e37738 instanceof Error)){
var e = e37738;
cljs.core._STAR_print_err_fn_STAR_.call(null,"Unable to load FIGWHEEL_CLIENT_CONFIGURATION from the environment");

return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"autoload","autoload",-354122500),false], null);
} else {
throw e37738;

}
}});
figwheel.client.console_intro_message = "Figwheel has compiled a temporary helper application to your :output-file.\n\nThe code currently in your configured output file does not\nrepresent the code that you are trying to compile.\n\nThis temporary application is intended to help you continue to get\nfeedback from Figwheel until the build you are working on compiles\ncorrectly.\n\nWhen your ClojureScript source code compiles correctly this helper\napplication will auto-reload and pick up your freshly compiled\nClojureScript program.";
figwheel.client.bad_compile_helper_app = (function figwheel$client$bad_compile_helper_app(){
cljs.core.enable_console_print_BANG_.call(null);

var config = figwheel.client.fetch_data_from_env.call(null);
cljs.core.println.call(null,figwheel.client.console_intro_message);

figwheel.client.heads_up.bad_compile_screen.call(null);

if(cljs.core.truth_(goog.dependencies_)){
} else {
goog.dependencies_ = true;
}

figwheel.client.start.call(null,config);

return figwheel.client.add_message_watch.call(null,new cljs.core.Keyword(null,"listen-for-successful-compile","listen-for-successful-compile",-995277603),((function (config){
return (function (p__37742){
var map__37743 = p__37742;
var map__37743__$1 = ((((!((map__37743 == null)))?((((map__37743.cljs$lang$protocol_mask$partition0$ & (64))) || (map__37743.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__37743):map__37743);
var msg_name = cljs.core.get.call(null,map__37743__$1,new cljs.core.Keyword(null,"msg-name","msg-name",-353709863));
if(cljs.core._EQ_.call(null,msg_name,new cljs.core.Keyword(null,"files-changed","files-changed",-1418200563))){
return location.href = location.href;
} else {
return null;
}
});})(config))
);
});

//# sourceMappingURL=client.js.map?rel=1485470176566