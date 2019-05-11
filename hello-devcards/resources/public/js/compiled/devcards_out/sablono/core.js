// Compiled by ClojureScript 1.9.229 {}
goog.provide('sablono.core');
goog.require('cljs.core');
goog.require('goog.dom');
goog.require('goog.string');
goog.require('sablono.normalize');
goog.require('sablono.util');
goog.require('sablono.interpreter');
goog.require('cljsjs.react');
goog.require('cljsjs.react.dom');
goog.require('clojure.string');
/**
 * Add an optional attribute argument to a function that returns a element vector.
 */
sablono.core.wrap_attrs = (function sablono$core$wrap_attrs(func){
return (function() { 
var G__28176__delegate = function (args){
if(cljs.core.map_QMARK_.call(null,cljs.core.first.call(null,args))){
var vec__28173 = cljs.core.apply.call(null,func,cljs.core.rest.call(null,args));
var seq__28174 = cljs.core.seq.call(null,vec__28173);
var first__28175 = cljs.core.first.call(null,seq__28174);
var seq__28174__$1 = cljs.core.next.call(null,seq__28174);
var tag = first__28175;
var body = seq__28174__$1;
if(cljs.core.map_QMARK_.call(null,cljs.core.first.call(null,body))){
return cljs.core.apply.call(null,cljs.core.vector,tag,cljs.core.merge.call(null,cljs.core.first.call(null,body),cljs.core.first.call(null,args)),cljs.core.rest.call(null,body));
} else {
return cljs.core.apply.call(null,cljs.core.vector,tag,cljs.core.first.call(null,args),body);
}
} else {
return cljs.core.apply.call(null,func,args);
}
};
var G__28176 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__28177__i = 0, G__28177__a = new Array(arguments.length -  0);
while (G__28177__i < G__28177__a.length) {G__28177__a[G__28177__i] = arguments[G__28177__i + 0]; ++G__28177__i;}
  args = new cljs.core.IndexedSeq(G__28177__a,0);
} 
return G__28176__delegate.call(this,args);};
G__28176.cljs$lang$maxFixedArity = 0;
G__28176.cljs$lang$applyTo = (function (arglist__28178){
var args = cljs.core.seq(arglist__28178);
return G__28176__delegate(args);
});
G__28176.cljs$core$IFn$_invoke$arity$variadic = G__28176__delegate;
return G__28176;
})()
;
});
sablono.core.update_arglists = (function sablono$core$update_arglists(arglists){
var iter__25570__auto__ = (function sablono$core$update_arglists_$_iter__28183(s__28184){
return (new cljs.core.LazySeq(null,(function (){
var s__28184__$1 = s__28184;
while(true){
var temp__4657__auto__ = cljs.core.seq.call(null,s__28184__$1);
if(temp__4657__auto__){
var s__28184__$2 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,s__28184__$2)){
var c__25568__auto__ = cljs.core.chunk_first.call(null,s__28184__$2);
var size__25569__auto__ = cljs.core.count.call(null,c__25568__auto__);
var b__28186 = cljs.core.chunk_buffer.call(null,size__25569__auto__);
if((function (){var i__28185 = (0);
while(true){
if((i__28185 < size__25569__auto__)){
var args = cljs.core._nth.call(null,c__25568__auto__,i__28185);
cljs.core.chunk_append.call(null,b__28186,cljs.core.vec.call(null,cljs.core.cons.call(null,new cljs.core.Symbol(null,"attr-map?","attr-map?",116307443,null),args)));

var G__28187 = (i__28185 + (1));
i__28185 = G__28187;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__28186),sablono$core$update_arglists_$_iter__28183.call(null,cljs.core.chunk_rest.call(null,s__28184__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__28186),null);
}
} else {
var args = cljs.core.first.call(null,s__28184__$2);
return cljs.core.cons.call(null,cljs.core.vec.call(null,cljs.core.cons.call(null,new cljs.core.Symbol(null,"attr-map?","attr-map?",116307443,null),args)),sablono$core$update_arglists_$_iter__28183.call(null,cljs.core.rest.call(null,s__28184__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__25570__auto__.call(null,arglists);
});
/**
 * Include a list of external stylesheet files.
 */
sablono.core.include_css = (function sablono$core$include_css(var_args){
var args__25872__auto__ = [];
var len__25865__auto___28193 = arguments.length;
var i__25866__auto___28194 = (0);
while(true){
if((i__25866__auto___28194 < len__25865__auto___28193)){
args__25872__auto__.push((arguments[i__25866__auto___28194]));

var G__28195 = (i__25866__auto___28194 + (1));
i__25866__auto___28194 = G__28195;
continue;
} else {
}
break;
}

var argseq__25873__auto__ = ((((0) < args__25872__auto__.length))?(new cljs.core.IndexedSeq(args__25872__auto__.slice((0)),(0),null)):null);
return sablono.core.include_css.cljs$core$IFn$_invoke$arity$variadic(argseq__25873__auto__);
});

sablono.core.include_css.cljs$core$IFn$_invoke$arity$variadic = (function (styles){
var iter__25570__auto__ = (function sablono$core$iter__28189(s__28190){
return (new cljs.core.LazySeq(null,(function (){
var s__28190__$1 = s__28190;
while(true){
var temp__4657__auto__ = cljs.core.seq.call(null,s__28190__$1);
if(temp__4657__auto__){
var s__28190__$2 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,s__28190__$2)){
var c__25568__auto__ = cljs.core.chunk_first.call(null,s__28190__$2);
var size__25569__auto__ = cljs.core.count.call(null,c__25568__auto__);
var b__28192 = cljs.core.chunk_buffer.call(null,size__25569__auto__);
if((function (){var i__28191 = (0);
while(true){
if((i__28191 < size__25569__auto__)){
var style = cljs.core._nth.call(null,c__25568__auto__,i__28191);
cljs.core.chunk_append.call(null,b__28192,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"link","link",-1769163468),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),"text/css",new cljs.core.Keyword(null,"href","href",-793805698),sablono.util.as_str.call(null,style),new cljs.core.Keyword(null,"rel","rel",1378823488),"stylesheet"], null)], null));

var G__28196 = (i__28191 + (1));
i__28191 = G__28196;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__28192),sablono$core$iter__28189.call(null,cljs.core.chunk_rest.call(null,s__28190__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__28192),null);
}
} else {
var style = cljs.core.first.call(null,s__28190__$2);
return cljs.core.cons.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"link","link",-1769163468),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),"text/css",new cljs.core.Keyword(null,"href","href",-793805698),sablono.util.as_str.call(null,style),new cljs.core.Keyword(null,"rel","rel",1378823488),"stylesheet"], null)], null),sablono$core$iter__28189.call(null,cljs.core.rest.call(null,s__28190__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__25570__auto__.call(null,styles);
});

sablono.core.include_css.cljs$lang$maxFixedArity = (0);

sablono.core.include_css.cljs$lang$applyTo = (function (seq28188){
return sablono.core.include_css.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq.call(null,seq28188));
});

/**
 * Include the JavaScript library at `src`.
 */
sablono.core.include_js = (function sablono$core$include_js(src){
return goog.dom.appendChild(goog.dom.getDocument().body,goog.dom.createDom("script",({"src": src})));
});
/**
 * Include Facebook's React JavaScript library.
 */
sablono.core.include_react = (function sablono$core$include_react(){
return sablono.core.include_js.call(null,"http://fb.me/react-0.12.2.js");
});
/**
 * Wraps some content in a HTML hyperlink with the supplied URL.
 */
sablono.core.link_to28197 = (function sablono$core$link_to28197(var_args){
var args__25872__auto__ = [];
var len__25865__auto___28200 = arguments.length;
var i__25866__auto___28201 = (0);
while(true){
if((i__25866__auto___28201 < len__25865__auto___28200)){
args__25872__auto__.push((arguments[i__25866__auto___28201]));

var G__28202 = (i__25866__auto___28201 + (1));
i__25866__auto___28201 = G__28202;
continue;
} else {
}
break;
}

var argseq__25873__auto__ = ((((1) < args__25872__auto__.length))?(new cljs.core.IndexedSeq(args__25872__auto__.slice((1)),(0),null)):null);
return sablono.core.link_to28197.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__25873__auto__);
});

sablono.core.link_to28197.cljs$core$IFn$_invoke$arity$variadic = (function (url,content){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"href","href",-793805698),sablono.util.as_str.call(null,url)], null),content], null);
});

sablono.core.link_to28197.cljs$lang$maxFixedArity = (1);

sablono.core.link_to28197.cljs$lang$applyTo = (function (seq28198){
var G__28199 = cljs.core.first.call(null,seq28198);
var seq28198__$1 = cljs.core.next.call(null,seq28198);
return sablono.core.link_to28197.cljs$core$IFn$_invoke$arity$variadic(G__28199,seq28198__$1);
});


sablono.core.link_to = sablono.core.wrap_attrs.call(null,sablono.core.link_to28197);
/**
 * Wraps some content in a HTML hyperlink with the supplied e-mail
 *   address. If no content provided use the e-mail address as content.
 */
sablono.core.mail_to28203 = (function sablono$core$mail_to28203(var_args){
var args__25872__auto__ = [];
var len__25865__auto___28210 = arguments.length;
var i__25866__auto___28211 = (0);
while(true){
if((i__25866__auto___28211 < len__25865__auto___28210)){
args__25872__auto__.push((arguments[i__25866__auto___28211]));

var G__28212 = (i__25866__auto___28211 + (1));
i__25866__auto___28211 = G__28212;
continue;
} else {
}
break;
}

var argseq__25873__auto__ = ((((1) < args__25872__auto__.length))?(new cljs.core.IndexedSeq(args__25872__auto__.slice((1)),(0),null)):null);
return sablono.core.mail_to28203.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__25873__auto__);
});

sablono.core.mail_to28203.cljs$core$IFn$_invoke$arity$variadic = (function (e_mail,p__28206){
var vec__28207 = p__28206;
var content = cljs.core.nth.call(null,vec__28207,(0),null);
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"href","href",-793805698),[cljs.core.str("mailto:"),cljs.core.str(e_mail)].join('')], null),(function (){var or__24790__auto__ = content;
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
return e_mail;
}
})()], null);
});

sablono.core.mail_to28203.cljs$lang$maxFixedArity = (1);

sablono.core.mail_to28203.cljs$lang$applyTo = (function (seq28204){
var G__28205 = cljs.core.first.call(null,seq28204);
var seq28204__$1 = cljs.core.next.call(null,seq28204);
return sablono.core.mail_to28203.cljs$core$IFn$_invoke$arity$variadic(G__28205,seq28204__$1);
});


sablono.core.mail_to = sablono.core.wrap_attrs.call(null,sablono.core.mail_to28203);
/**
 * Wrap a collection in an unordered list.
 */
sablono.core.unordered_list28213 = (function sablono$core$unordered_list28213(coll){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ul","ul",-1349521403),(function (){var iter__25570__auto__ = (function sablono$core$unordered_list28213_$_iter__28218(s__28219){
return (new cljs.core.LazySeq(null,(function (){
var s__28219__$1 = s__28219;
while(true){
var temp__4657__auto__ = cljs.core.seq.call(null,s__28219__$1);
if(temp__4657__auto__){
var s__28219__$2 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,s__28219__$2)){
var c__25568__auto__ = cljs.core.chunk_first.call(null,s__28219__$2);
var size__25569__auto__ = cljs.core.count.call(null,c__25568__auto__);
var b__28221 = cljs.core.chunk_buffer.call(null,size__25569__auto__);
if((function (){var i__28220 = (0);
while(true){
if((i__28220 < size__25569__auto__)){
var x = cljs.core._nth.call(null,c__25568__auto__,i__28220);
cljs.core.chunk_append.call(null,b__28221,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),x], null));

var G__28222 = (i__28220 + (1));
i__28220 = G__28222;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__28221),sablono$core$unordered_list28213_$_iter__28218.call(null,cljs.core.chunk_rest.call(null,s__28219__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__28221),null);
}
} else {
var x = cljs.core.first.call(null,s__28219__$2);
return cljs.core.cons.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),x], null),sablono$core$unordered_list28213_$_iter__28218.call(null,cljs.core.rest.call(null,s__28219__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__25570__auto__.call(null,coll);
})()], null);
});

sablono.core.unordered_list = sablono.core.wrap_attrs.call(null,sablono.core.unordered_list28213);
/**
 * Wrap a collection in an ordered list.
 */
sablono.core.ordered_list28223 = (function sablono$core$ordered_list28223(coll){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ol","ol",932524051),(function (){var iter__25570__auto__ = (function sablono$core$ordered_list28223_$_iter__28228(s__28229){
return (new cljs.core.LazySeq(null,(function (){
var s__28229__$1 = s__28229;
while(true){
var temp__4657__auto__ = cljs.core.seq.call(null,s__28229__$1);
if(temp__4657__auto__){
var s__28229__$2 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,s__28229__$2)){
var c__25568__auto__ = cljs.core.chunk_first.call(null,s__28229__$2);
var size__25569__auto__ = cljs.core.count.call(null,c__25568__auto__);
var b__28231 = cljs.core.chunk_buffer.call(null,size__25569__auto__);
if((function (){var i__28230 = (0);
while(true){
if((i__28230 < size__25569__auto__)){
var x = cljs.core._nth.call(null,c__25568__auto__,i__28230);
cljs.core.chunk_append.call(null,b__28231,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),x], null));

var G__28232 = (i__28230 + (1));
i__28230 = G__28232;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__28231),sablono$core$ordered_list28223_$_iter__28228.call(null,cljs.core.chunk_rest.call(null,s__28229__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__28231),null);
}
} else {
var x = cljs.core.first.call(null,s__28229__$2);
return cljs.core.cons.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),x], null),sablono$core$ordered_list28223_$_iter__28228.call(null,cljs.core.rest.call(null,s__28229__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__25570__auto__.call(null,coll);
})()], null);
});

sablono.core.ordered_list = sablono.core.wrap_attrs.call(null,sablono.core.ordered_list28223);
/**
 * Create an image element.
 */
sablono.core.image28233 = (function sablono$core$image28233(var_args){
var args28234 = [];
var len__25865__auto___28237 = arguments.length;
var i__25866__auto___28238 = (0);
while(true){
if((i__25866__auto___28238 < len__25865__auto___28237)){
args28234.push((arguments[i__25866__auto___28238]));

var G__28239 = (i__25866__auto___28238 + (1));
i__25866__auto___28238 = G__28239;
continue;
} else {
}
break;
}

var G__28236 = args28234.length;
switch (G__28236) {
case 1:
return sablono.core.image28233.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.image28233.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28234.length)].join('')));

}
});

sablono.core.image28233.cljs$core$IFn$_invoke$arity$1 = (function (src){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img","img",1442687358),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"src","src",-1651076051),sablono.util.as_str.call(null,src)], null)], null);
});

sablono.core.image28233.cljs$core$IFn$_invoke$arity$2 = (function (src,alt){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img","img",1442687358),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"src","src",-1651076051),sablono.util.as_str.call(null,src),new cljs.core.Keyword(null,"alt","alt",-3214426),alt], null)], null);
});

sablono.core.image28233.cljs$lang$maxFixedArity = 2;


sablono.core.image = sablono.core.wrap_attrs.call(null,sablono.core.image28233);
sablono.core._STAR_group_STAR_ = cljs.core.PersistentVector.EMPTY;
/**
 * Create a field name from the supplied argument the current field group.
 */
sablono.core.make_name = (function sablono$core$make_name(name){
return cljs.core.reduce.call(null,(function (p1__28241_SHARP_,p2__28242_SHARP_){
return [cljs.core.str(p1__28241_SHARP_),cljs.core.str("["),cljs.core.str(p2__28242_SHARP_),cljs.core.str("]")].join('');
}),cljs.core.conj.call(null,sablono.core._STAR_group_STAR_,sablono.util.as_str.call(null,name)));
});
/**
 * Create a field id from the supplied argument and current field group.
 */
sablono.core.make_id = (function sablono$core$make_id(name){
return cljs.core.reduce.call(null,(function (p1__28243_SHARP_,p2__28244_SHARP_){
return [cljs.core.str(p1__28243_SHARP_),cljs.core.str("-"),cljs.core.str(p2__28244_SHARP_)].join('');
}),cljs.core.conj.call(null,sablono.core._STAR_group_STAR_,sablono.util.as_str.call(null,name)));
});
/**
 * Creates a new <input> element.
 */
sablono.core.input_field_STAR_ = (function sablono$core$input_field_STAR_(type,name,value){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"type","type",1174270348),type,new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name.call(null,name),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id.call(null,name),new cljs.core.Keyword(null,"value","value",305978217),(function (){var or__24790__auto__ = value;
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
return undefined;
}
})()], null)], null);
});
/**
 * Creates a color input field.
 */
sablono.core.color_field28245 = (function sablono$core$color_field28245(var_args){
var args28246 = [];
var len__25865__auto___28313 = arguments.length;
var i__25866__auto___28314 = (0);
while(true){
if((i__25866__auto___28314 < len__25865__auto___28313)){
args28246.push((arguments[i__25866__auto___28314]));

var G__28315 = (i__25866__auto___28314 + (1));
i__25866__auto___28314 = G__28315;
continue;
} else {
}
break;
}

var G__28248 = args28246.length;
switch (G__28248) {
case 1:
return sablono.core.color_field28245.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.color_field28245.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28246.length)].join('')));

}
});

sablono.core.color_field28245.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.color_field28245.call(null,name__28160__auto__,null);
});

sablono.core.color_field28245.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"color","color",-1642760596,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.color_field28245.cljs$lang$maxFixedArity = 2;


sablono.core.color_field = sablono.core.wrap_attrs.call(null,sablono.core.color_field28245);

/**
 * Creates a date input field.
 */
sablono.core.date_field28249 = (function sablono$core$date_field28249(var_args){
var args28250 = [];
var len__25865__auto___28317 = arguments.length;
var i__25866__auto___28318 = (0);
while(true){
if((i__25866__auto___28318 < len__25865__auto___28317)){
args28250.push((arguments[i__25866__auto___28318]));

var G__28319 = (i__25866__auto___28318 + (1));
i__25866__auto___28318 = G__28319;
continue;
} else {
}
break;
}

var G__28252 = args28250.length;
switch (G__28252) {
case 1:
return sablono.core.date_field28249.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.date_field28249.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28250.length)].join('')));

}
});

sablono.core.date_field28249.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.date_field28249.call(null,name__28160__auto__,null);
});

sablono.core.date_field28249.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"date","date",177097065,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.date_field28249.cljs$lang$maxFixedArity = 2;


sablono.core.date_field = sablono.core.wrap_attrs.call(null,sablono.core.date_field28249);

/**
 * Creates a datetime input field.
 */
sablono.core.datetime_field28253 = (function sablono$core$datetime_field28253(var_args){
var args28254 = [];
var len__25865__auto___28321 = arguments.length;
var i__25866__auto___28322 = (0);
while(true){
if((i__25866__auto___28322 < len__25865__auto___28321)){
args28254.push((arguments[i__25866__auto___28322]));

var G__28323 = (i__25866__auto___28322 + (1));
i__25866__auto___28322 = G__28323;
continue;
} else {
}
break;
}

var G__28256 = args28254.length;
switch (G__28256) {
case 1:
return sablono.core.datetime_field28253.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.datetime_field28253.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28254.length)].join('')));

}
});

sablono.core.datetime_field28253.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.datetime_field28253.call(null,name__28160__auto__,null);
});

sablono.core.datetime_field28253.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"datetime","datetime",2135207229,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.datetime_field28253.cljs$lang$maxFixedArity = 2;


sablono.core.datetime_field = sablono.core.wrap_attrs.call(null,sablono.core.datetime_field28253);

/**
 * Creates a datetime-local input field.
 */
sablono.core.datetime_local_field28257 = (function sablono$core$datetime_local_field28257(var_args){
var args28258 = [];
var len__25865__auto___28325 = arguments.length;
var i__25866__auto___28326 = (0);
while(true){
if((i__25866__auto___28326 < len__25865__auto___28325)){
args28258.push((arguments[i__25866__auto___28326]));

var G__28327 = (i__25866__auto___28326 + (1));
i__25866__auto___28326 = G__28327;
continue;
} else {
}
break;
}

var G__28260 = args28258.length;
switch (G__28260) {
case 1:
return sablono.core.datetime_local_field28257.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.datetime_local_field28257.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28258.length)].join('')));

}
});

sablono.core.datetime_local_field28257.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.datetime_local_field28257.call(null,name__28160__auto__,null);
});

sablono.core.datetime_local_field28257.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"datetime-local","datetime-local",-507312697,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.datetime_local_field28257.cljs$lang$maxFixedArity = 2;


sablono.core.datetime_local_field = sablono.core.wrap_attrs.call(null,sablono.core.datetime_local_field28257);

/**
 * Creates a email input field.
 */
sablono.core.email_field28261 = (function sablono$core$email_field28261(var_args){
var args28262 = [];
var len__25865__auto___28329 = arguments.length;
var i__25866__auto___28330 = (0);
while(true){
if((i__25866__auto___28330 < len__25865__auto___28329)){
args28262.push((arguments[i__25866__auto___28330]));

var G__28331 = (i__25866__auto___28330 + (1));
i__25866__auto___28330 = G__28331;
continue;
} else {
}
break;
}

var G__28264 = args28262.length;
switch (G__28264) {
case 1:
return sablono.core.email_field28261.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.email_field28261.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28262.length)].join('')));

}
});

sablono.core.email_field28261.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.email_field28261.call(null,name__28160__auto__,null);
});

sablono.core.email_field28261.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"email","email",-1238619063,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.email_field28261.cljs$lang$maxFixedArity = 2;


sablono.core.email_field = sablono.core.wrap_attrs.call(null,sablono.core.email_field28261);

/**
 * Creates a file input field.
 */
sablono.core.file_field28265 = (function sablono$core$file_field28265(var_args){
var args28266 = [];
var len__25865__auto___28333 = arguments.length;
var i__25866__auto___28334 = (0);
while(true){
if((i__25866__auto___28334 < len__25865__auto___28333)){
args28266.push((arguments[i__25866__auto___28334]));

var G__28335 = (i__25866__auto___28334 + (1));
i__25866__auto___28334 = G__28335;
continue;
} else {
}
break;
}

var G__28268 = args28266.length;
switch (G__28268) {
case 1:
return sablono.core.file_field28265.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.file_field28265.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28266.length)].join('')));

}
});

sablono.core.file_field28265.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.file_field28265.call(null,name__28160__auto__,null);
});

sablono.core.file_field28265.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"file","file",370885649,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.file_field28265.cljs$lang$maxFixedArity = 2;


sablono.core.file_field = sablono.core.wrap_attrs.call(null,sablono.core.file_field28265);

/**
 * Creates a hidden input field.
 */
sablono.core.hidden_field28269 = (function sablono$core$hidden_field28269(var_args){
var args28270 = [];
var len__25865__auto___28337 = arguments.length;
var i__25866__auto___28338 = (0);
while(true){
if((i__25866__auto___28338 < len__25865__auto___28337)){
args28270.push((arguments[i__25866__auto___28338]));

var G__28339 = (i__25866__auto___28338 + (1));
i__25866__auto___28338 = G__28339;
continue;
} else {
}
break;
}

var G__28272 = args28270.length;
switch (G__28272) {
case 1:
return sablono.core.hidden_field28269.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.hidden_field28269.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28270.length)].join('')));

}
});

sablono.core.hidden_field28269.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.hidden_field28269.call(null,name__28160__auto__,null);
});

sablono.core.hidden_field28269.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"hidden","hidden",1328025435,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.hidden_field28269.cljs$lang$maxFixedArity = 2;


sablono.core.hidden_field = sablono.core.wrap_attrs.call(null,sablono.core.hidden_field28269);

/**
 * Creates a month input field.
 */
sablono.core.month_field28273 = (function sablono$core$month_field28273(var_args){
var args28274 = [];
var len__25865__auto___28341 = arguments.length;
var i__25866__auto___28342 = (0);
while(true){
if((i__25866__auto___28342 < len__25865__auto___28341)){
args28274.push((arguments[i__25866__auto___28342]));

var G__28343 = (i__25866__auto___28342 + (1));
i__25866__auto___28342 = G__28343;
continue;
} else {
}
break;
}

var G__28276 = args28274.length;
switch (G__28276) {
case 1:
return sablono.core.month_field28273.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.month_field28273.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28274.length)].join('')));

}
});

sablono.core.month_field28273.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.month_field28273.call(null,name__28160__auto__,null);
});

sablono.core.month_field28273.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"month","month",-319717006,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.month_field28273.cljs$lang$maxFixedArity = 2;


sablono.core.month_field = sablono.core.wrap_attrs.call(null,sablono.core.month_field28273);

/**
 * Creates a number input field.
 */
sablono.core.number_field28277 = (function sablono$core$number_field28277(var_args){
var args28278 = [];
var len__25865__auto___28345 = arguments.length;
var i__25866__auto___28346 = (0);
while(true){
if((i__25866__auto___28346 < len__25865__auto___28345)){
args28278.push((arguments[i__25866__auto___28346]));

var G__28347 = (i__25866__auto___28346 + (1));
i__25866__auto___28346 = G__28347;
continue;
} else {
}
break;
}

var G__28280 = args28278.length;
switch (G__28280) {
case 1:
return sablono.core.number_field28277.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.number_field28277.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28278.length)].join('')));

}
});

sablono.core.number_field28277.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.number_field28277.call(null,name__28160__auto__,null);
});

sablono.core.number_field28277.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"number","number",-1084057331,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.number_field28277.cljs$lang$maxFixedArity = 2;


sablono.core.number_field = sablono.core.wrap_attrs.call(null,sablono.core.number_field28277);

/**
 * Creates a password input field.
 */
sablono.core.password_field28281 = (function sablono$core$password_field28281(var_args){
var args28282 = [];
var len__25865__auto___28349 = arguments.length;
var i__25866__auto___28350 = (0);
while(true){
if((i__25866__auto___28350 < len__25865__auto___28349)){
args28282.push((arguments[i__25866__auto___28350]));

var G__28351 = (i__25866__auto___28350 + (1));
i__25866__auto___28350 = G__28351;
continue;
} else {
}
break;
}

var G__28284 = args28282.length;
switch (G__28284) {
case 1:
return sablono.core.password_field28281.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.password_field28281.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28282.length)].join('')));

}
});

sablono.core.password_field28281.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.password_field28281.call(null,name__28160__auto__,null);
});

sablono.core.password_field28281.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"password","password",2057553998,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.password_field28281.cljs$lang$maxFixedArity = 2;


sablono.core.password_field = sablono.core.wrap_attrs.call(null,sablono.core.password_field28281);

/**
 * Creates a range input field.
 */
sablono.core.range_field28285 = (function sablono$core$range_field28285(var_args){
var args28286 = [];
var len__25865__auto___28353 = arguments.length;
var i__25866__auto___28354 = (0);
while(true){
if((i__25866__auto___28354 < len__25865__auto___28353)){
args28286.push((arguments[i__25866__auto___28354]));

var G__28355 = (i__25866__auto___28354 + (1));
i__25866__auto___28354 = G__28355;
continue;
} else {
}
break;
}

var G__28288 = args28286.length;
switch (G__28288) {
case 1:
return sablono.core.range_field28285.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.range_field28285.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28286.length)].join('')));

}
});

sablono.core.range_field28285.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.range_field28285.call(null,name__28160__auto__,null);
});

sablono.core.range_field28285.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"range","range",-1014743483,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.range_field28285.cljs$lang$maxFixedArity = 2;


sablono.core.range_field = sablono.core.wrap_attrs.call(null,sablono.core.range_field28285);

/**
 * Creates a search input field.
 */
sablono.core.search_field28289 = (function sablono$core$search_field28289(var_args){
var args28290 = [];
var len__25865__auto___28357 = arguments.length;
var i__25866__auto___28358 = (0);
while(true){
if((i__25866__auto___28358 < len__25865__auto___28357)){
args28290.push((arguments[i__25866__auto___28358]));

var G__28359 = (i__25866__auto___28358 + (1));
i__25866__auto___28358 = G__28359;
continue;
} else {
}
break;
}

var G__28292 = args28290.length;
switch (G__28292) {
case 1:
return sablono.core.search_field28289.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.search_field28289.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28290.length)].join('')));

}
});

sablono.core.search_field28289.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.search_field28289.call(null,name__28160__auto__,null);
});

sablono.core.search_field28289.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"search","search",-1089495947,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.search_field28289.cljs$lang$maxFixedArity = 2;


sablono.core.search_field = sablono.core.wrap_attrs.call(null,sablono.core.search_field28289);

/**
 * Creates a tel input field.
 */
sablono.core.tel_field28293 = (function sablono$core$tel_field28293(var_args){
var args28294 = [];
var len__25865__auto___28361 = arguments.length;
var i__25866__auto___28362 = (0);
while(true){
if((i__25866__auto___28362 < len__25865__auto___28361)){
args28294.push((arguments[i__25866__auto___28362]));

var G__28363 = (i__25866__auto___28362 + (1));
i__25866__auto___28362 = G__28363;
continue;
} else {
}
break;
}

var G__28296 = args28294.length;
switch (G__28296) {
case 1:
return sablono.core.tel_field28293.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.tel_field28293.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28294.length)].join('')));

}
});

sablono.core.tel_field28293.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.tel_field28293.call(null,name__28160__auto__,null);
});

sablono.core.tel_field28293.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"tel","tel",1864669686,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.tel_field28293.cljs$lang$maxFixedArity = 2;


sablono.core.tel_field = sablono.core.wrap_attrs.call(null,sablono.core.tel_field28293);

/**
 * Creates a text input field.
 */
sablono.core.text_field28297 = (function sablono$core$text_field28297(var_args){
var args28298 = [];
var len__25865__auto___28365 = arguments.length;
var i__25866__auto___28366 = (0);
while(true){
if((i__25866__auto___28366 < len__25865__auto___28365)){
args28298.push((arguments[i__25866__auto___28366]));

var G__28367 = (i__25866__auto___28366 + (1));
i__25866__auto___28366 = G__28367;
continue;
} else {
}
break;
}

var G__28300 = args28298.length;
switch (G__28300) {
case 1:
return sablono.core.text_field28297.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.text_field28297.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28298.length)].join('')));

}
});

sablono.core.text_field28297.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.text_field28297.call(null,name__28160__auto__,null);
});

sablono.core.text_field28297.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"text","text",-150030170,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.text_field28297.cljs$lang$maxFixedArity = 2;


sablono.core.text_field = sablono.core.wrap_attrs.call(null,sablono.core.text_field28297);

/**
 * Creates a time input field.
 */
sablono.core.time_field28301 = (function sablono$core$time_field28301(var_args){
var args28302 = [];
var len__25865__auto___28369 = arguments.length;
var i__25866__auto___28370 = (0);
while(true){
if((i__25866__auto___28370 < len__25865__auto___28369)){
args28302.push((arguments[i__25866__auto___28370]));

var G__28371 = (i__25866__auto___28370 + (1));
i__25866__auto___28370 = G__28371;
continue;
} else {
}
break;
}

var G__28304 = args28302.length;
switch (G__28304) {
case 1:
return sablono.core.time_field28301.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.time_field28301.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28302.length)].join('')));

}
});

sablono.core.time_field28301.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.time_field28301.call(null,name__28160__auto__,null);
});

sablono.core.time_field28301.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"time","time",-1268547887,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.time_field28301.cljs$lang$maxFixedArity = 2;


sablono.core.time_field = sablono.core.wrap_attrs.call(null,sablono.core.time_field28301);

/**
 * Creates a url input field.
 */
sablono.core.url_field28305 = (function sablono$core$url_field28305(var_args){
var args28306 = [];
var len__25865__auto___28373 = arguments.length;
var i__25866__auto___28374 = (0);
while(true){
if((i__25866__auto___28374 < len__25865__auto___28373)){
args28306.push((arguments[i__25866__auto___28374]));

var G__28375 = (i__25866__auto___28374 + (1));
i__25866__auto___28374 = G__28375;
continue;
} else {
}
break;
}

var G__28308 = args28306.length;
switch (G__28308) {
case 1:
return sablono.core.url_field28305.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.url_field28305.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28306.length)].join('')));

}
});

sablono.core.url_field28305.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.url_field28305.call(null,name__28160__auto__,null);
});

sablono.core.url_field28305.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"url","url",1916828573,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.url_field28305.cljs$lang$maxFixedArity = 2;


sablono.core.url_field = sablono.core.wrap_attrs.call(null,sablono.core.url_field28305);

/**
 * Creates a week input field.
 */
sablono.core.week_field28309 = (function sablono$core$week_field28309(var_args){
var args28310 = [];
var len__25865__auto___28377 = arguments.length;
var i__25866__auto___28378 = (0);
while(true){
if((i__25866__auto___28378 < len__25865__auto___28377)){
args28310.push((arguments[i__25866__auto___28378]));

var G__28379 = (i__25866__auto___28378 + (1));
i__25866__auto___28378 = G__28379;
continue;
} else {
}
break;
}

var G__28312 = args28310.length;
switch (G__28312) {
case 1:
return sablono.core.week_field28309.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.week_field28309.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28310.length)].join('')));

}
});

sablono.core.week_field28309.cljs$core$IFn$_invoke$arity$1 = (function (name__28160__auto__){
return sablono.core.week_field28309.call(null,name__28160__auto__,null);
});

sablono.core.week_field28309.cljs$core$IFn$_invoke$arity$2 = (function (name__28160__auto__,value__28161__auto__){
return sablono.core.input_field_STAR_.call(null,[cljs.core.str(new cljs.core.Symbol(null,"week","week",314058249,null))].join(''),name__28160__auto__,value__28161__auto__);
});

sablono.core.week_field28309.cljs$lang$maxFixedArity = 2;


sablono.core.week_field = sablono.core.wrap_attrs.call(null,sablono.core.week_field28309);
sablono.core.file_upload = sablono.core.file_field;
/**
 * Creates a check box.
 */
sablono.core.check_box28381 = (function sablono$core$check_box28381(var_args){
var args28382 = [];
var len__25865__auto___28385 = arguments.length;
var i__25866__auto___28386 = (0);
while(true){
if((i__25866__auto___28386 < len__25865__auto___28385)){
args28382.push((arguments[i__25866__auto___28386]));

var G__28387 = (i__25866__auto___28386 + (1));
i__25866__auto___28386 = G__28387;
continue;
} else {
}
break;
}

var G__28384 = args28382.length;
switch (G__28384) {
case 1:
return sablono.core.check_box28381.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.check_box28381.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return sablono.core.check_box28381.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28382.length)].join('')));

}
});

sablono.core.check_box28381.cljs$core$IFn$_invoke$arity$1 = (function (name){
return sablono.core.check_box28381.call(null,name,null);
});

sablono.core.check_box28381.cljs$core$IFn$_invoke$arity$2 = (function (name,checked_QMARK_){
return sablono.core.check_box28381.call(null,name,checked_QMARK_,"true");
});

sablono.core.check_box28381.cljs$core$IFn$_invoke$arity$3 = (function (name,checked_QMARK_,value){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"type","type",1174270348),"checkbox",new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name.call(null,name),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id.call(null,name),new cljs.core.Keyword(null,"value","value",305978217),(function (){var or__24790__auto__ = value;
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
return undefined;
}
})(),new cljs.core.Keyword(null,"checked","checked",-50955819),checked_QMARK_], null)], null);
});

sablono.core.check_box28381.cljs$lang$maxFixedArity = 3;


sablono.core.check_box = sablono.core.wrap_attrs.call(null,sablono.core.check_box28381);
/**
 * Creates a radio button.
 */
sablono.core.radio_button28389 = (function sablono$core$radio_button28389(var_args){
var args28390 = [];
var len__25865__auto___28393 = arguments.length;
var i__25866__auto___28394 = (0);
while(true){
if((i__25866__auto___28394 < len__25865__auto___28393)){
args28390.push((arguments[i__25866__auto___28394]));

var G__28395 = (i__25866__auto___28394 + (1));
i__25866__auto___28394 = G__28395;
continue;
} else {
}
break;
}

var G__28392 = args28390.length;
switch (G__28392) {
case 1:
return sablono.core.radio_button28389.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.radio_button28389.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return sablono.core.radio_button28389.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28390.length)].join('')));

}
});

sablono.core.radio_button28389.cljs$core$IFn$_invoke$arity$1 = (function (group){
return sablono.core.radio_button28389.call(null,group,null);
});

sablono.core.radio_button28389.cljs$core$IFn$_invoke$arity$2 = (function (group,checked_QMARK_){
return sablono.core.radio_button28389.call(null,group,checked_QMARK_,"true");
});

sablono.core.radio_button28389.cljs$core$IFn$_invoke$arity$3 = (function (group,checked_QMARK_,value){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"type","type",1174270348),"radio",new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name.call(null,group),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id.call(null,[cljs.core.str(sablono.util.as_str.call(null,group)),cljs.core.str("-"),cljs.core.str(sablono.util.as_str.call(null,value))].join('')),new cljs.core.Keyword(null,"value","value",305978217),(function (){var or__24790__auto__ = value;
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
return undefined;
}
})(),new cljs.core.Keyword(null,"checked","checked",-50955819),checked_QMARK_], null)], null);
});

sablono.core.radio_button28389.cljs$lang$maxFixedArity = 3;


sablono.core.radio_button = sablono.core.wrap_attrs.call(null,sablono.core.radio_button28389);
sablono.core.hash_key = (function sablono$core$hash_key(x){
return goog.string.hashCode(cljs.core.pr_str.call(null,x));
});
/**
 * Creates a seq of option tags from a collection.
 */
sablono.core.select_options28397 = (function sablono$core$select_options28397(coll){
var iter__25570__auto__ = (function sablono$core$select_options28397_$_iter__28414(s__28415){
return (new cljs.core.LazySeq(null,(function (){
var s__28415__$1 = s__28415;
while(true){
var temp__4657__auto__ = cljs.core.seq.call(null,s__28415__$1);
if(temp__4657__auto__){
var s__28415__$2 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,s__28415__$2)){
var c__25568__auto__ = cljs.core.chunk_first.call(null,s__28415__$2);
var size__25569__auto__ = cljs.core.count.call(null,c__25568__auto__);
var b__28417 = cljs.core.chunk_buffer.call(null,size__25569__auto__);
if((function (){var i__28416 = (0);
while(true){
if((i__28416 < size__25569__auto__)){
var x = cljs.core._nth.call(null,c__25568__auto__,i__28416);
cljs.core.chunk_append.call(null,b__28417,((cljs.core.sequential_QMARK_.call(null,x))?(function (){var vec__28424 = x;
var text = cljs.core.nth.call(null,vec__28424,(0),null);
var val = cljs.core.nth.call(null,vec__28424,(1),null);
var disabled_QMARK_ = cljs.core.nth.call(null,vec__28424,(2),null);
var disabled_QMARK___$1 = cljs.core.boolean$.call(null,disabled_QMARK_);
if(cljs.core.sequential_QMARK_.call(null,val)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"optgroup","optgroup",1738282218),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),sablono.core.hash_key.call(null,text),new cljs.core.Keyword(null,"label","label",1718410804),text], null),sablono$core$select_options28397.call(null,val)], null);
} else {
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"disabled","disabled",-1529784218),disabled_QMARK___$1,new cljs.core.Keyword(null,"key","key",-1516042587),sablono.core.hash_key.call(null,val),new cljs.core.Keyword(null,"value","value",305978217),val], null),text], null);
}
})():new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),sablono.core.hash_key.call(null,x),new cljs.core.Keyword(null,"value","value",305978217),x], null),x], null)));

var G__28430 = (i__28416 + (1));
i__28416 = G__28430;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__28417),sablono$core$select_options28397_$_iter__28414.call(null,cljs.core.chunk_rest.call(null,s__28415__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__28417),null);
}
} else {
var x = cljs.core.first.call(null,s__28415__$2);
return cljs.core.cons.call(null,((cljs.core.sequential_QMARK_.call(null,x))?(function (){var vec__28427 = x;
var text = cljs.core.nth.call(null,vec__28427,(0),null);
var val = cljs.core.nth.call(null,vec__28427,(1),null);
var disabled_QMARK_ = cljs.core.nth.call(null,vec__28427,(2),null);
var disabled_QMARK___$1 = cljs.core.boolean$.call(null,disabled_QMARK_);
if(cljs.core.sequential_QMARK_.call(null,val)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"optgroup","optgroup",1738282218),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),sablono.core.hash_key.call(null,text),new cljs.core.Keyword(null,"label","label",1718410804),text], null),sablono$core$select_options28397.call(null,val)], null);
} else {
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"disabled","disabled",-1529784218),disabled_QMARK___$1,new cljs.core.Keyword(null,"key","key",-1516042587),sablono.core.hash_key.call(null,val),new cljs.core.Keyword(null,"value","value",305978217),val], null),text], null);
}
})():new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),sablono.core.hash_key.call(null,x),new cljs.core.Keyword(null,"value","value",305978217),x], null),x], null)),sablono$core$select_options28397_$_iter__28414.call(null,cljs.core.rest.call(null,s__28415__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__25570__auto__.call(null,coll);
});

sablono.core.select_options = sablono.core.wrap_attrs.call(null,sablono.core.select_options28397);
/**
 * Creates a drop-down box using the <select> tag.
 */
sablono.core.drop_down28431 = (function sablono$core$drop_down28431(var_args){
var args28432 = [];
var len__25865__auto___28435 = arguments.length;
var i__25866__auto___28436 = (0);
while(true){
if((i__25866__auto___28436 < len__25865__auto___28435)){
args28432.push((arguments[i__25866__auto___28436]));

var G__28437 = (i__25866__auto___28436 + (1));
i__25866__auto___28436 = G__28437;
continue;
} else {
}
break;
}

var G__28434 = args28432.length;
switch (G__28434) {
case 2:
return sablono.core.drop_down28431.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return sablono.core.drop_down28431.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28432.length)].join('')));

}
});

sablono.core.drop_down28431.cljs$core$IFn$_invoke$arity$2 = (function (name,options){
return sablono.core.drop_down28431.call(null,name,options,null);
});

sablono.core.drop_down28431.cljs$core$IFn$_invoke$arity$3 = (function (name,options,selected){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"select","select",1147833503),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name.call(null,name),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id.call(null,name)], null),sablono.core.select_options.call(null,options,selected)], null);
});

sablono.core.drop_down28431.cljs$lang$maxFixedArity = 3;


sablono.core.drop_down = sablono.core.wrap_attrs.call(null,sablono.core.drop_down28431);
/**
 * Creates a text area element.
 */
sablono.core.text_area28439 = (function sablono$core$text_area28439(var_args){
var args28440 = [];
var len__25865__auto___28443 = arguments.length;
var i__25866__auto___28444 = (0);
while(true){
if((i__25866__auto___28444 < len__25865__auto___28443)){
args28440.push((arguments[i__25866__auto___28444]));

var G__28445 = (i__25866__auto___28444 + (1));
i__25866__auto___28444 = G__28445;
continue;
} else {
}
break;
}

var G__28442 = args28440.length;
switch (G__28442) {
case 1:
return sablono.core.text_area28439.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.text_area28439.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args28440.length)].join('')));

}
});

sablono.core.text_area28439.cljs$core$IFn$_invoke$arity$1 = (function (name){
return sablono.core.text_area28439.call(null,name,null);
});

sablono.core.text_area28439.cljs$core$IFn$_invoke$arity$2 = (function (name,value){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"textarea","textarea",-650375824),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name.call(null,name),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id.call(null,name),new cljs.core.Keyword(null,"value","value",305978217),(function (){var or__24790__auto__ = value;
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
return undefined;
}
})()], null)], null);
});

sablono.core.text_area28439.cljs$lang$maxFixedArity = 2;


sablono.core.text_area = sablono.core.wrap_attrs.call(null,sablono.core.text_area28439);
/**
 * Creates a label for an input field with the supplied name.
 */
sablono.core.label28447 = (function sablono$core$label28447(name,text){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"htmlFor","htmlFor",-1050291720),sablono.core.make_id.call(null,name)], null),text], null);
});

sablono.core.label = sablono.core.wrap_attrs.call(null,sablono.core.label28447);
/**
 * Creates a submit button.
 */
sablono.core.submit_button28448 = (function sablono$core$submit_button28448(text){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),"submit",new cljs.core.Keyword(null,"value","value",305978217),text], null)], null);
});

sablono.core.submit_button = sablono.core.wrap_attrs.call(null,sablono.core.submit_button28448);
/**
 * Creates a form reset button.
 */
sablono.core.reset_button28449 = (function sablono$core$reset_button28449(text){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),"reset",new cljs.core.Keyword(null,"value","value",305978217),text], null)], null);
});

sablono.core.reset_button = sablono.core.wrap_attrs.call(null,sablono.core.reset_button28449);
/**
 * Create a form that points to a particular method and route.
 *   e.g. (form-to [:put "/post"]
 *       ...)
 */
sablono.core.form_to28450 = (function sablono$core$form_to28450(var_args){
var args__25872__auto__ = [];
var len__25865__auto___28457 = arguments.length;
var i__25866__auto___28458 = (0);
while(true){
if((i__25866__auto___28458 < len__25865__auto___28457)){
args__25872__auto__.push((arguments[i__25866__auto___28458]));

var G__28459 = (i__25866__auto___28458 + (1));
i__25866__auto___28458 = G__28459;
continue;
} else {
}
break;
}

var argseq__25873__auto__ = ((((1) < args__25872__auto__.length))?(new cljs.core.IndexedSeq(args__25872__auto__.slice((1)),(0),null)):null);
return sablono.core.form_to28450.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__25873__auto__);
});

sablono.core.form_to28450.cljs$core$IFn$_invoke$arity$variadic = (function (p__28453,body){
var vec__28454 = p__28453;
var method = cljs.core.nth.call(null,vec__28454,(0),null);
var action = cljs.core.nth.call(null,vec__28454,(1),null);
var method_str = clojure.string.upper_case.call(null,cljs.core.name.call(null,method));
var action_uri = sablono.util.to_uri.call(null,action);
return cljs.core.vec.call(null,cljs.core.concat.call(null,((cljs.core.contains_QMARK_.call(null,new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"get","get",1683182755),null,new cljs.core.Keyword(null,"post","post",269697687),null], null), null),method))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"form","form",-1624062471),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"method","method",55703592),method_str,new cljs.core.Keyword(null,"action","action",-811238024),action_uri], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"form","form",-1624062471),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"method","method",55703592),"POST",new cljs.core.Keyword(null,"action","action",-811238024),action_uri], null),sablono.core.hidden_field.call(null,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),(3735928559)], null),"_method",method_str)], null)),body));
});

sablono.core.form_to28450.cljs$lang$maxFixedArity = (1);

sablono.core.form_to28450.cljs$lang$applyTo = (function (seq28451){
var G__28452 = cljs.core.first.call(null,seq28451);
var seq28451__$1 = cljs.core.next.call(null,seq28451);
return sablono.core.form_to28450.cljs$core$IFn$_invoke$arity$variadic(G__28452,seq28451__$1);
});


sablono.core.form_to = sablono.core.wrap_attrs.call(null,sablono.core.form_to28450);

//# sourceMappingURL=core.js.map?rel=1485470169647