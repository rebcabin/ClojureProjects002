// Compiled by ClojureScript 1.9.229 {}
goog.provide('cljs.repl');
goog.require('cljs.core');
goog.require('cljs.spec');
cljs.repl.print_doc = (function cljs$repl$print_doc(p__36330){
var map__36355 = p__36330;
var map__36355__$1 = ((((!((map__36355 == null)))?((((map__36355.cljs$lang$protocol_mask$partition0$ & (64))) || (map__36355.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__36355):map__36355);
var m = map__36355__$1;
var n = cljs.core.get.call(null,map__36355__$1,new cljs.core.Keyword(null,"ns","ns",441598760));
var nm = cljs.core.get.call(null,map__36355__$1,new cljs.core.Keyword(null,"name","name",1843675177));
cljs.core.println.call(null,"-------------------------");

cljs.core.println.call(null,[cljs.core.str((function (){var temp__4657__auto__ = new cljs.core.Keyword(null,"ns","ns",441598760).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(temp__4657__auto__)){
var ns = temp__4657__auto__;
return [cljs.core.str(ns),cljs.core.str("/")].join('');
} else {
return null;
}
})()),cljs.core.str(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m))].join(''));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"Protocol");
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m))){
var seq__36357_36379 = cljs.core.seq.call(null,new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m));
var chunk__36358_36380 = null;
var count__36359_36381 = (0);
var i__36360_36382 = (0);
while(true){
if((i__36360_36382 < count__36359_36381)){
var f_36383 = cljs.core._nth.call(null,chunk__36358_36380,i__36360_36382);
cljs.core.println.call(null,"  ",f_36383);

var G__36384 = seq__36357_36379;
var G__36385 = chunk__36358_36380;
var G__36386 = count__36359_36381;
var G__36387 = (i__36360_36382 + (1));
seq__36357_36379 = G__36384;
chunk__36358_36380 = G__36385;
count__36359_36381 = G__36386;
i__36360_36382 = G__36387;
continue;
} else {
var temp__4657__auto___36388 = cljs.core.seq.call(null,seq__36357_36379);
if(temp__4657__auto___36388){
var seq__36357_36389__$1 = temp__4657__auto___36388;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__36357_36389__$1)){
var c__25601__auto___36390 = cljs.core.chunk_first.call(null,seq__36357_36389__$1);
var G__36391 = cljs.core.chunk_rest.call(null,seq__36357_36389__$1);
var G__36392 = c__25601__auto___36390;
var G__36393 = cljs.core.count.call(null,c__25601__auto___36390);
var G__36394 = (0);
seq__36357_36379 = G__36391;
chunk__36358_36380 = G__36392;
count__36359_36381 = G__36393;
i__36360_36382 = G__36394;
continue;
} else {
var f_36395 = cljs.core.first.call(null,seq__36357_36389__$1);
cljs.core.println.call(null,"  ",f_36395);

var G__36396 = cljs.core.next.call(null,seq__36357_36389__$1);
var G__36397 = null;
var G__36398 = (0);
var G__36399 = (0);
seq__36357_36379 = G__36396;
chunk__36358_36380 = G__36397;
count__36359_36381 = G__36398;
i__36360_36382 = G__36399;
continue;
}
} else {
}
}
break;
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m))){
var arglists_36400 = new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_((function (){var or__24790__auto__ = new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
return new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m);
}
})())){
cljs.core.prn.call(null,arglists_36400);
} else {
cljs.core.prn.call(null,((cljs.core._EQ_.call(null,new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.first.call(null,arglists_36400)))?cljs.core.second.call(null,arglists_36400):arglists_36400));
}
} else {
}
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"special-form","special-form",-1326536374).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"Special Form");

cljs.core.println.call(null," ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m));

if(cljs.core.contains_QMARK_.call(null,m,new cljs.core.Keyword(null,"url","url",276297046))){
if(cljs.core.truth_(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m))){
return cljs.core.println.call(null,[cljs.core.str("\n  Please see http://clojure.org/"),cljs.core.str(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m))].join(''));
} else {
return null;
}
} else {
return cljs.core.println.call(null,[cljs.core.str("\n  Please see http://clojure.org/special_forms#"),cljs.core.str(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m))].join(''));
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"Macro");
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"REPL Special Function");
} else {
}

cljs.core.println.call(null," ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
var seq__36361_36401 = cljs.core.seq.call(null,new cljs.core.Keyword(null,"methods","methods",453930866).cljs$core$IFn$_invoke$arity$1(m));
var chunk__36362_36402 = null;
var count__36363_36403 = (0);
var i__36364_36404 = (0);
while(true){
if((i__36364_36404 < count__36363_36403)){
var vec__36365_36405 = cljs.core._nth.call(null,chunk__36362_36402,i__36364_36404);
var name_36406 = cljs.core.nth.call(null,vec__36365_36405,(0),null);
var map__36368_36407 = cljs.core.nth.call(null,vec__36365_36405,(1),null);
var map__36368_36408__$1 = ((((!((map__36368_36407 == null)))?((((map__36368_36407.cljs$lang$protocol_mask$partition0$ & (64))) || (map__36368_36407.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__36368_36407):map__36368_36407);
var doc_36409 = cljs.core.get.call(null,map__36368_36408__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists_36410 = cljs.core.get.call(null,map__36368_36408__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println.call(null);

cljs.core.println.call(null," ",name_36406);

cljs.core.println.call(null," ",arglists_36410);

if(cljs.core.truth_(doc_36409)){
cljs.core.println.call(null," ",doc_36409);
} else {
}

var G__36411 = seq__36361_36401;
var G__36412 = chunk__36362_36402;
var G__36413 = count__36363_36403;
var G__36414 = (i__36364_36404 + (1));
seq__36361_36401 = G__36411;
chunk__36362_36402 = G__36412;
count__36363_36403 = G__36413;
i__36364_36404 = G__36414;
continue;
} else {
var temp__4657__auto___36415 = cljs.core.seq.call(null,seq__36361_36401);
if(temp__4657__auto___36415){
var seq__36361_36416__$1 = temp__4657__auto___36415;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__36361_36416__$1)){
var c__25601__auto___36417 = cljs.core.chunk_first.call(null,seq__36361_36416__$1);
var G__36418 = cljs.core.chunk_rest.call(null,seq__36361_36416__$1);
var G__36419 = c__25601__auto___36417;
var G__36420 = cljs.core.count.call(null,c__25601__auto___36417);
var G__36421 = (0);
seq__36361_36401 = G__36418;
chunk__36362_36402 = G__36419;
count__36363_36403 = G__36420;
i__36364_36404 = G__36421;
continue;
} else {
var vec__36370_36422 = cljs.core.first.call(null,seq__36361_36416__$1);
var name_36423 = cljs.core.nth.call(null,vec__36370_36422,(0),null);
var map__36373_36424 = cljs.core.nth.call(null,vec__36370_36422,(1),null);
var map__36373_36425__$1 = ((((!((map__36373_36424 == null)))?((((map__36373_36424.cljs$lang$protocol_mask$partition0$ & (64))) || (map__36373_36424.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__36373_36424):map__36373_36424);
var doc_36426 = cljs.core.get.call(null,map__36373_36425__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists_36427 = cljs.core.get.call(null,map__36373_36425__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println.call(null);

cljs.core.println.call(null," ",name_36423);

cljs.core.println.call(null," ",arglists_36427);

if(cljs.core.truth_(doc_36426)){
cljs.core.println.call(null," ",doc_36426);
} else {
}

var G__36428 = cljs.core.next.call(null,seq__36361_36416__$1);
var G__36429 = null;
var G__36430 = (0);
var G__36431 = (0);
seq__36361_36401 = G__36428;
chunk__36362_36402 = G__36429;
count__36363_36403 = G__36430;
i__36364_36404 = G__36431;
continue;
}
} else {
}
}
break;
}
} else {
}

if(cljs.core.truth_(n)){
var temp__4657__auto__ = cljs.spec.get_spec.call(null,cljs.core.symbol.call(null,[cljs.core.str(cljs.core.ns_name.call(null,n))].join(''),cljs.core.name.call(null,nm)));
if(cljs.core.truth_(temp__4657__auto__)){
var fnspec = temp__4657__auto__;
cljs.core.print.call(null,"Spec");

var seq__36375 = cljs.core.seq.call(null,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"args","args",1315556576),new cljs.core.Keyword(null,"ret","ret",-468222814),new cljs.core.Keyword(null,"fn","fn",-1175266204)], null));
var chunk__36376 = null;
var count__36377 = (0);
var i__36378 = (0);
while(true){
if((i__36378 < count__36377)){
var role = cljs.core._nth.call(null,chunk__36376,i__36378);
var temp__4657__auto___36432__$1 = cljs.core.get.call(null,fnspec,role);
if(cljs.core.truth_(temp__4657__auto___36432__$1)){
var spec_36433 = temp__4657__auto___36432__$1;
cljs.core.print.call(null,[cljs.core.str("\n "),cljs.core.str(cljs.core.name.call(null,role)),cljs.core.str(":")].join(''),cljs.spec.describe.call(null,spec_36433));
} else {
}

var G__36434 = seq__36375;
var G__36435 = chunk__36376;
var G__36436 = count__36377;
var G__36437 = (i__36378 + (1));
seq__36375 = G__36434;
chunk__36376 = G__36435;
count__36377 = G__36436;
i__36378 = G__36437;
continue;
} else {
var temp__4657__auto____$1 = cljs.core.seq.call(null,seq__36375);
if(temp__4657__auto____$1){
var seq__36375__$1 = temp__4657__auto____$1;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__36375__$1)){
var c__25601__auto__ = cljs.core.chunk_first.call(null,seq__36375__$1);
var G__36438 = cljs.core.chunk_rest.call(null,seq__36375__$1);
var G__36439 = c__25601__auto__;
var G__36440 = cljs.core.count.call(null,c__25601__auto__);
var G__36441 = (0);
seq__36375 = G__36438;
chunk__36376 = G__36439;
count__36377 = G__36440;
i__36378 = G__36441;
continue;
} else {
var role = cljs.core.first.call(null,seq__36375__$1);
var temp__4657__auto___36442__$2 = cljs.core.get.call(null,fnspec,role);
if(cljs.core.truth_(temp__4657__auto___36442__$2)){
var spec_36443 = temp__4657__auto___36442__$2;
cljs.core.print.call(null,[cljs.core.str("\n "),cljs.core.str(cljs.core.name.call(null,role)),cljs.core.str(":")].join(''),cljs.spec.describe.call(null,spec_36443));
} else {
}

var G__36444 = cljs.core.next.call(null,seq__36375__$1);
var G__36445 = null;
var G__36446 = (0);
var G__36447 = (0);
seq__36375 = G__36444;
chunk__36376 = G__36445;
count__36377 = G__36446;
i__36378 = G__36447;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return null;
}
} else {
return null;
}
}
});

//# sourceMappingURL=repl.js.map?rel=1485470175594