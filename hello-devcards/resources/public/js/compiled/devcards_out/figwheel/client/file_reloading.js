// Compiled by ClojureScript 1.9.229 {}
goog.provide('figwheel.client.file_reloading');
goog.require('cljs.core');
goog.require('goog.string');
goog.require('goog.Uri');
goog.require('goog.net.jsloader');
goog.require('cljs.core.async');
goog.require('goog.object');
goog.require('clojure.set');
goog.require('clojure.string');
goog.require('figwheel.client.utils');
if(typeof figwheel.client.file_reloading.figwheel_meta_pragmas !== 'undefined'){
} else {
figwheel.client.file_reloading.figwheel_meta_pragmas = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
}
figwheel.client.file_reloading.on_jsload_custom_event = (function figwheel$client$file_reloading$on_jsload_custom_event(url){
return figwheel.client.utils.dispatch_custom_event.call(null,"figwheel.js-reload",url);
});
figwheel.client.file_reloading.before_jsload_custom_event = (function figwheel$client$file_reloading$before_jsload_custom_event(files){
return figwheel.client.utils.dispatch_custom_event.call(null,"figwheel.before-js-reload",files);
});
figwheel.client.file_reloading.on_cssload_custom_event = (function figwheel$client$file_reloading$on_cssload_custom_event(files){
return figwheel.client.utils.dispatch_custom_event.call(null,"figwheel.css-reload",files);
});
figwheel.client.file_reloading.namespace_file_map_QMARK_ = (function figwheel$client$file_reloading$namespace_file_map_QMARK_(m){
var or__24790__auto__ = (cljs.core.map_QMARK_.call(null,m)) && (typeof new cljs.core.Keyword(null,"namespace","namespace",-377510372).cljs$core$IFn$_invoke$arity$1(m) === 'string') && (((new cljs.core.Keyword(null,"file","file",-1269645878).cljs$core$IFn$_invoke$arity$1(m) == null)) || (typeof new cljs.core.Keyword(null,"file","file",-1269645878).cljs$core$IFn$_invoke$arity$1(m) === 'string')) && (cljs.core._EQ_.call(null,new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(m),new cljs.core.Keyword(null,"namespace","namespace",-377510372)));
if(or__24790__auto__){
return or__24790__auto__;
} else {
cljs.core.println.call(null,"Error not namespace-file-map",cljs.core.pr_str.call(null,m));

return false;
}
});
figwheel.client.file_reloading.add_cache_buster = (function figwheel$client$file_reloading$add_cache_buster(url){

return goog.Uri.parse(url).makeUnique();
});
figwheel.client.file_reloading.name__GT_path = (function figwheel$client$file_reloading$name__GT_path(ns){

return (goog.dependencies_.nameToPath[ns]);
});
figwheel.client.file_reloading.provided_QMARK_ = (function figwheel$client$file_reloading$provided_QMARK_(ns){
return (goog.dependencies_.written[figwheel.client.file_reloading.name__GT_path.call(null,ns)]);
});
figwheel.client.file_reloading.fix_node_request_url = (function figwheel$client$file_reloading$fix_node_request_url(url){

if(cljs.core.truth_(goog.string.startsWith(url,"../"))){
return clojure.string.replace.call(null,url,"../","");
} else {
return [cljs.core.str("goog/"),cljs.core.str(url)].join('');
}
});
figwheel.client.file_reloading.immutable_ns_QMARK_ = (function figwheel$client$file_reloading$immutable_ns_QMARK_(name){
var or__24790__auto__ = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 9, ["svgpan.SvgPan",null,"far.out",null,"testDep.bar",null,"someprotopackage.TestPackageTypes",null,"goog",null,"an.existing.path",null,"cljs.core",null,"ns",null,"dup.base",null], null), null).call(null,name);
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
return cljs.core.some.call(null,cljs.core.partial.call(null,goog.string.startsWith,name),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, ["goog.","cljs.","clojure.","fake.","proto2."], null));
}
});
figwheel.client.file_reloading.get_requires = (function figwheel$client$file_reloading$get_requires(ns){
return cljs.core.set.call(null,cljs.core.filter.call(null,(function (p1__33960_SHARP_){
return cljs.core.not.call(null,figwheel.client.file_reloading.immutable_ns_QMARK_.call(null,p1__33960_SHARP_));
}),goog.object.getKeys((goog.dependencies_.requires[figwheel.client.file_reloading.name__GT_path.call(null,ns)]))));
});
if(typeof figwheel.client.file_reloading.dependency_data !== 'undefined'){
} else {
figwheel.client.file_reloading.dependency_data = cljs.core.atom.call(null,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"pathToName","pathToName",-1236616181),cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"dependents","dependents",136812837),cljs.core.PersistentArrayMap.EMPTY], null));
}
figwheel.client.file_reloading.path_to_name_BANG_ = (function figwheel$client$file_reloading$path_to_name_BANG_(path,name){
return cljs.core.swap_BANG_.call(null,figwheel.client.file_reloading.dependency_data,cljs.core.update_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"pathToName","pathToName",-1236616181),path], null),cljs.core.fnil.call(null,clojure.set.union,cljs.core.PersistentHashSet.EMPTY),cljs.core.PersistentHashSet.fromArray([name], true));
});
/**
 * Setup a path to name dependencies map.
 * That goes from path -> #{ ns-names }
 */
figwheel.client.file_reloading.setup_path__GT_name_BANG_ = (function figwheel$client$file_reloading$setup_path__GT_name_BANG_(){
var nameToPath = goog.object.filter(goog.dependencies_.nameToPath,(function (v,k,o){
return goog.string.startsWith(v,"../");
}));
return goog.object.forEach(nameToPath,((function (nameToPath){
return (function (v,k,o){
return figwheel.client.file_reloading.path_to_name_BANG_.call(null,v,k);
});})(nameToPath))
);
});
/**
 * returns a set of namespaces defined by a path
 */
figwheel.client.file_reloading.path__GT_name = (function figwheel$client$file_reloading$path__GT_name(path){
return cljs.core.get_in.call(null,cljs.core.deref.call(null,figwheel.client.file_reloading.dependency_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"pathToName","pathToName",-1236616181),path], null));
});
figwheel.client.file_reloading.name_to_parent_BANG_ = (function figwheel$client$file_reloading$name_to_parent_BANG_(ns,parent_ns){
return cljs.core.swap_BANG_.call(null,figwheel.client.file_reloading.dependency_data,cljs.core.update_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"dependents","dependents",136812837),ns], null),cljs.core.fnil.call(null,clojure.set.union,cljs.core.PersistentHashSet.EMPTY),cljs.core.PersistentHashSet.fromArray([parent_ns], true));
});
/**
 * This reverses the goog.dependencies_.requires for looking up ns-dependents.
 */
figwheel.client.file_reloading.setup_ns__GT_dependents_BANG_ = (function figwheel$client$file_reloading$setup_ns__GT_dependents_BANG_(){
var requires = goog.object.filter(goog.dependencies_.requires,(function (v,k,o){
return goog.string.startsWith(k,"../");
}));
return goog.object.forEach(requires,((function (requires){
return (function (v,k,_){
return goog.object.forEach(v,((function (requires){
return (function (v_SINGLEQUOTE_,k_SINGLEQUOTE_,___$1){
var seq__33965 = cljs.core.seq.call(null,figwheel.client.file_reloading.path__GT_name.call(null,k));
var chunk__33966 = null;
var count__33967 = (0);
var i__33968 = (0);
while(true){
if((i__33968 < count__33967)){
var n = cljs.core._nth.call(null,chunk__33966,i__33968);
figwheel.client.file_reloading.name_to_parent_BANG_.call(null,k_SINGLEQUOTE_,n);

var G__33969 = seq__33965;
var G__33970 = chunk__33966;
var G__33971 = count__33967;
var G__33972 = (i__33968 + (1));
seq__33965 = G__33969;
chunk__33966 = G__33970;
count__33967 = G__33971;
i__33968 = G__33972;
continue;
} else {
var temp__4657__auto__ = cljs.core.seq.call(null,seq__33965);
if(temp__4657__auto__){
var seq__33965__$1 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__33965__$1)){
var c__25601__auto__ = cljs.core.chunk_first.call(null,seq__33965__$1);
var G__33973 = cljs.core.chunk_rest.call(null,seq__33965__$1);
var G__33974 = c__25601__auto__;
var G__33975 = cljs.core.count.call(null,c__25601__auto__);
var G__33976 = (0);
seq__33965 = G__33973;
chunk__33966 = G__33974;
count__33967 = G__33975;
i__33968 = G__33976;
continue;
} else {
var n = cljs.core.first.call(null,seq__33965__$1);
figwheel.client.file_reloading.name_to_parent_BANG_.call(null,k_SINGLEQUOTE_,n);

var G__33977 = cljs.core.next.call(null,seq__33965__$1);
var G__33978 = null;
var G__33979 = (0);
var G__33980 = (0);
seq__33965 = G__33977;
chunk__33966 = G__33978;
count__33967 = G__33979;
i__33968 = G__33980;
continue;
}
} else {
return null;
}
}
break;
}
});})(requires))
);
});})(requires))
);
});
figwheel.client.file_reloading.ns__GT_dependents = (function figwheel$client$file_reloading$ns__GT_dependents(ns){
return cljs.core.get_in.call(null,cljs.core.deref.call(null,figwheel.client.file_reloading.dependency_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"dependents","dependents",136812837),ns], null));
});
figwheel.client.file_reloading.build_topo_sort = (function figwheel$client$file_reloading$build_topo_sort(get_deps){
var get_deps__$1 = cljs.core.memoize.call(null,get_deps);
var topo_sort_helper_STAR_ = ((function (get_deps__$1){
return (function figwheel$client$file_reloading$build_topo_sort_$_topo_sort_helper_STAR_(x,depth,state){
var deps = get_deps__$1.call(null,x);
if(cljs.core.empty_QMARK_.call(null,deps)){
return null;
} else {
return topo_sort_STAR_.call(null,deps,depth,state);
}
});})(get_deps__$1))
;
var topo_sort_STAR_ = ((function (get_deps__$1){
return (function() {
var figwheel$client$file_reloading$build_topo_sort_$_topo_sort_STAR_ = null;
var figwheel$client$file_reloading$build_topo_sort_$_topo_sort_STAR___1 = (function (deps){
return figwheel$client$file_reloading$build_topo_sort_$_topo_sort_STAR_.call(null,deps,(0),cljs.core.atom.call(null,cljs.core.sorted_map.call(null)));
});
var figwheel$client$file_reloading$build_topo_sort_$_topo_sort_STAR___3 = (function (deps,depth,state){
cljs.core.swap_BANG_.call(null,state,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [depth], null),cljs.core.fnil.call(null,cljs.core.into,cljs.core.PersistentHashSet.EMPTY),deps);

var seq__34031_34042 = cljs.core.seq.call(null,deps);
var chunk__34032_34043 = null;
var count__34033_34044 = (0);
var i__34034_34045 = (0);
while(true){
if((i__34034_34045 < count__34033_34044)){
var dep_34046 = cljs.core._nth.call(null,chunk__34032_34043,i__34034_34045);
topo_sort_helper_STAR_.call(null,dep_34046,(depth + (1)),state);

var G__34047 = seq__34031_34042;
var G__34048 = chunk__34032_34043;
var G__34049 = count__34033_34044;
var G__34050 = (i__34034_34045 + (1));
seq__34031_34042 = G__34047;
chunk__34032_34043 = G__34048;
count__34033_34044 = G__34049;
i__34034_34045 = G__34050;
continue;
} else {
var temp__4657__auto___34051 = cljs.core.seq.call(null,seq__34031_34042);
if(temp__4657__auto___34051){
var seq__34031_34052__$1 = temp__4657__auto___34051;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__34031_34052__$1)){
var c__25601__auto___34053 = cljs.core.chunk_first.call(null,seq__34031_34052__$1);
var G__34054 = cljs.core.chunk_rest.call(null,seq__34031_34052__$1);
var G__34055 = c__25601__auto___34053;
var G__34056 = cljs.core.count.call(null,c__25601__auto___34053);
var G__34057 = (0);
seq__34031_34042 = G__34054;
chunk__34032_34043 = G__34055;
count__34033_34044 = G__34056;
i__34034_34045 = G__34057;
continue;
} else {
var dep_34058 = cljs.core.first.call(null,seq__34031_34052__$1);
topo_sort_helper_STAR_.call(null,dep_34058,(depth + (1)),state);

var G__34059 = cljs.core.next.call(null,seq__34031_34052__$1);
var G__34060 = null;
var G__34061 = (0);
var G__34062 = (0);
seq__34031_34042 = G__34059;
chunk__34032_34043 = G__34060;
count__34033_34044 = G__34061;
i__34034_34045 = G__34062;
continue;
}
} else {
}
}
break;
}

if(cljs.core._EQ_.call(null,depth,(0))){
return elim_dups_STAR_.call(null,cljs.core.reverse.call(null,cljs.core.vals.call(null,cljs.core.deref.call(null,state))));
} else {
return null;
}
});
figwheel$client$file_reloading$build_topo_sort_$_topo_sort_STAR_ = function(deps,depth,state){
switch(arguments.length){
case 1:
return figwheel$client$file_reloading$build_topo_sort_$_topo_sort_STAR___1.call(this,deps);
case 3:
return figwheel$client$file_reloading$build_topo_sort_$_topo_sort_STAR___3.call(this,deps,depth,state);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
figwheel$client$file_reloading$build_topo_sort_$_topo_sort_STAR_.cljs$core$IFn$_invoke$arity$1 = figwheel$client$file_reloading$build_topo_sort_$_topo_sort_STAR___1;
figwheel$client$file_reloading$build_topo_sort_$_topo_sort_STAR_.cljs$core$IFn$_invoke$arity$3 = figwheel$client$file_reloading$build_topo_sort_$_topo_sort_STAR___3;
return figwheel$client$file_reloading$build_topo_sort_$_topo_sort_STAR_;
})()
;})(get_deps__$1))
;
var elim_dups_STAR_ = ((function (get_deps__$1){
return (function figwheel$client$file_reloading$build_topo_sort_$_elim_dups_STAR_(p__34035){
var vec__34039 = p__34035;
var seq__34040 = cljs.core.seq.call(null,vec__34039);
var first__34041 = cljs.core.first.call(null,seq__34040);
var seq__34040__$1 = cljs.core.next.call(null,seq__34040);
var x = first__34041;
var xs = seq__34040__$1;
if((x == null)){
return cljs.core.List.EMPTY;
} else {
return cljs.core.cons.call(null,x,figwheel$client$file_reloading$build_topo_sort_$_elim_dups_STAR_.call(null,cljs.core.map.call(null,((function (vec__34039,seq__34040,first__34041,seq__34040__$1,x,xs,get_deps__$1){
return (function (p1__33981_SHARP_){
return clojure.set.difference.call(null,p1__33981_SHARP_,x);
});})(vec__34039,seq__34040,first__34041,seq__34040__$1,x,xs,get_deps__$1))
,xs)));
}
});})(get_deps__$1))
;
return topo_sort_STAR_;
});
figwheel.client.file_reloading.get_all_dependencies = (function figwheel$client$file_reloading$get_all_dependencies(ns){
var topo_sort_SINGLEQUOTE_ = figwheel.client.file_reloading.build_topo_sort.call(null,figwheel.client.file_reloading.get_requires);
return cljs.core.apply.call(null,cljs.core.concat,topo_sort_SINGLEQUOTE_.call(null,cljs.core.set.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [ns], null))));
});
figwheel.client.file_reloading.get_all_dependents = (function figwheel$client$file_reloading$get_all_dependents(nss){
var topo_sort_SINGLEQUOTE_ = figwheel.client.file_reloading.build_topo_sort.call(null,figwheel.client.file_reloading.ns__GT_dependents);
return cljs.core.reverse.call(null,cljs.core.apply.call(null,cljs.core.concat,topo_sort_SINGLEQUOTE_.call(null,cljs.core.set.call(null,nss))));
});
figwheel.client.file_reloading.unprovide_BANG_ = (function figwheel$client$file_reloading$unprovide_BANG_(ns){
var path = figwheel.client.file_reloading.name__GT_path.call(null,ns);
goog.object.remove(goog.dependencies_.visited,path);

goog.object.remove(goog.dependencies_.written,path);

return goog.object.remove(goog.dependencies_.written,[cljs.core.str(goog.basePath),cljs.core.str(path)].join(''));
});
figwheel.client.file_reloading.resolve_ns = (function figwheel$client$file_reloading$resolve_ns(ns){
return [cljs.core.str(goog.basePath),cljs.core.str(figwheel.client.file_reloading.name__GT_path.call(null,ns))].join('');
});
figwheel.client.file_reloading.addDependency = (function figwheel$client$file_reloading$addDependency(path,provides,requires){
var seq__34075 = cljs.core.seq.call(null,provides);
var chunk__34076 = null;
var count__34077 = (0);
var i__34078 = (0);
while(true){
if((i__34078 < count__34077)){
var prov = cljs.core._nth.call(null,chunk__34076,i__34078);
figwheel.client.file_reloading.path_to_name_BANG_.call(null,path,prov);

var seq__34079_34087 = cljs.core.seq.call(null,requires);
var chunk__34080_34088 = null;
var count__34081_34089 = (0);
var i__34082_34090 = (0);
while(true){
if((i__34082_34090 < count__34081_34089)){
var req_34091 = cljs.core._nth.call(null,chunk__34080_34088,i__34082_34090);
figwheel.client.file_reloading.name_to_parent_BANG_.call(null,req_34091,prov);

var G__34092 = seq__34079_34087;
var G__34093 = chunk__34080_34088;
var G__34094 = count__34081_34089;
var G__34095 = (i__34082_34090 + (1));
seq__34079_34087 = G__34092;
chunk__34080_34088 = G__34093;
count__34081_34089 = G__34094;
i__34082_34090 = G__34095;
continue;
} else {
var temp__4657__auto___34096 = cljs.core.seq.call(null,seq__34079_34087);
if(temp__4657__auto___34096){
var seq__34079_34097__$1 = temp__4657__auto___34096;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__34079_34097__$1)){
var c__25601__auto___34098 = cljs.core.chunk_first.call(null,seq__34079_34097__$1);
var G__34099 = cljs.core.chunk_rest.call(null,seq__34079_34097__$1);
var G__34100 = c__25601__auto___34098;
var G__34101 = cljs.core.count.call(null,c__25601__auto___34098);
var G__34102 = (0);
seq__34079_34087 = G__34099;
chunk__34080_34088 = G__34100;
count__34081_34089 = G__34101;
i__34082_34090 = G__34102;
continue;
} else {
var req_34103 = cljs.core.first.call(null,seq__34079_34097__$1);
figwheel.client.file_reloading.name_to_parent_BANG_.call(null,req_34103,prov);

var G__34104 = cljs.core.next.call(null,seq__34079_34097__$1);
var G__34105 = null;
var G__34106 = (0);
var G__34107 = (0);
seq__34079_34087 = G__34104;
chunk__34080_34088 = G__34105;
count__34081_34089 = G__34106;
i__34082_34090 = G__34107;
continue;
}
} else {
}
}
break;
}

var G__34108 = seq__34075;
var G__34109 = chunk__34076;
var G__34110 = count__34077;
var G__34111 = (i__34078 + (1));
seq__34075 = G__34108;
chunk__34076 = G__34109;
count__34077 = G__34110;
i__34078 = G__34111;
continue;
} else {
var temp__4657__auto__ = cljs.core.seq.call(null,seq__34075);
if(temp__4657__auto__){
var seq__34075__$1 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__34075__$1)){
var c__25601__auto__ = cljs.core.chunk_first.call(null,seq__34075__$1);
var G__34112 = cljs.core.chunk_rest.call(null,seq__34075__$1);
var G__34113 = c__25601__auto__;
var G__34114 = cljs.core.count.call(null,c__25601__auto__);
var G__34115 = (0);
seq__34075 = G__34112;
chunk__34076 = G__34113;
count__34077 = G__34114;
i__34078 = G__34115;
continue;
} else {
var prov = cljs.core.first.call(null,seq__34075__$1);
figwheel.client.file_reloading.path_to_name_BANG_.call(null,path,prov);

var seq__34083_34116 = cljs.core.seq.call(null,requires);
var chunk__34084_34117 = null;
var count__34085_34118 = (0);
var i__34086_34119 = (0);
while(true){
if((i__34086_34119 < count__34085_34118)){
var req_34120 = cljs.core._nth.call(null,chunk__34084_34117,i__34086_34119);
figwheel.client.file_reloading.name_to_parent_BANG_.call(null,req_34120,prov);

var G__34121 = seq__34083_34116;
var G__34122 = chunk__34084_34117;
var G__34123 = count__34085_34118;
var G__34124 = (i__34086_34119 + (1));
seq__34083_34116 = G__34121;
chunk__34084_34117 = G__34122;
count__34085_34118 = G__34123;
i__34086_34119 = G__34124;
continue;
} else {
var temp__4657__auto___34125__$1 = cljs.core.seq.call(null,seq__34083_34116);
if(temp__4657__auto___34125__$1){
var seq__34083_34126__$1 = temp__4657__auto___34125__$1;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__34083_34126__$1)){
var c__25601__auto___34127 = cljs.core.chunk_first.call(null,seq__34083_34126__$1);
var G__34128 = cljs.core.chunk_rest.call(null,seq__34083_34126__$1);
var G__34129 = c__25601__auto___34127;
var G__34130 = cljs.core.count.call(null,c__25601__auto___34127);
var G__34131 = (0);
seq__34083_34116 = G__34128;
chunk__34084_34117 = G__34129;
count__34085_34118 = G__34130;
i__34086_34119 = G__34131;
continue;
} else {
var req_34132 = cljs.core.first.call(null,seq__34083_34126__$1);
figwheel.client.file_reloading.name_to_parent_BANG_.call(null,req_34132,prov);

var G__34133 = cljs.core.next.call(null,seq__34083_34126__$1);
var G__34134 = null;
var G__34135 = (0);
var G__34136 = (0);
seq__34083_34116 = G__34133;
chunk__34084_34117 = G__34134;
count__34085_34118 = G__34135;
i__34086_34119 = G__34136;
continue;
}
} else {
}
}
break;
}

var G__34137 = cljs.core.next.call(null,seq__34075__$1);
var G__34138 = null;
var G__34139 = (0);
var G__34140 = (0);
seq__34075 = G__34137;
chunk__34076 = G__34138;
count__34077 = G__34139;
i__34078 = G__34140;
continue;
}
} else {
return null;
}
}
break;
}
});
figwheel.client.file_reloading.figwheel_require = (function figwheel$client$file_reloading$figwheel_require(src,reload){
goog.require = figwheel$client$file_reloading$figwheel_require;

if(cljs.core._EQ_.call(null,reload,"reload-all")){
var seq__34145_34149 = cljs.core.seq.call(null,figwheel.client.file_reloading.get_all_dependencies.call(null,src));
var chunk__34146_34150 = null;
var count__34147_34151 = (0);
var i__34148_34152 = (0);
while(true){
if((i__34148_34152 < count__34147_34151)){
var ns_34153 = cljs.core._nth.call(null,chunk__34146_34150,i__34148_34152);
figwheel.client.file_reloading.unprovide_BANG_.call(null,ns_34153);

var G__34154 = seq__34145_34149;
var G__34155 = chunk__34146_34150;
var G__34156 = count__34147_34151;
var G__34157 = (i__34148_34152 + (1));
seq__34145_34149 = G__34154;
chunk__34146_34150 = G__34155;
count__34147_34151 = G__34156;
i__34148_34152 = G__34157;
continue;
} else {
var temp__4657__auto___34158 = cljs.core.seq.call(null,seq__34145_34149);
if(temp__4657__auto___34158){
var seq__34145_34159__$1 = temp__4657__auto___34158;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__34145_34159__$1)){
var c__25601__auto___34160 = cljs.core.chunk_first.call(null,seq__34145_34159__$1);
var G__34161 = cljs.core.chunk_rest.call(null,seq__34145_34159__$1);
var G__34162 = c__25601__auto___34160;
var G__34163 = cljs.core.count.call(null,c__25601__auto___34160);
var G__34164 = (0);
seq__34145_34149 = G__34161;
chunk__34146_34150 = G__34162;
count__34147_34151 = G__34163;
i__34148_34152 = G__34164;
continue;
} else {
var ns_34165 = cljs.core.first.call(null,seq__34145_34159__$1);
figwheel.client.file_reloading.unprovide_BANG_.call(null,ns_34165);

var G__34166 = cljs.core.next.call(null,seq__34145_34159__$1);
var G__34167 = null;
var G__34168 = (0);
var G__34169 = (0);
seq__34145_34149 = G__34166;
chunk__34146_34150 = G__34167;
count__34147_34151 = G__34168;
i__34148_34152 = G__34169;
continue;
}
} else {
}
}
break;
}
} else {
}

if(cljs.core.truth_(reload)){
figwheel.client.file_reloading.unprovide_BANG_.call(null,src);
} else {
}

return goog.require_figwheel_backup_(src);
});
/**
 * Reusable browser REPL bootstrapping. Patches the essential functions
 *   in goog.base to support re-loading of namespaces after page load.
 */
figwheel.client.file_reloading.bootstrap_goog_base = (function figwheel$client$file_reloading$bootstrap_goog_base(){
if(cljs.core.truth_(COMPILED)){
return null;
} else {
goog.require_figwheel_backup_ = (function (){var or__24790__auto__ = goog.require__;
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
return goog.require;
}
})();

goog.isProvided_ = (function (name){
return false;
});

figwheel.client.file_reloading.setup_path__GT_name_BANG_.call(null);

figwheel.client.file_reloading.setup_ns__GT_dependents_BANG_.call(null);

goog.addDependency_figwheel_backup_ = goog.addDependency;

goog.addDependency = (function() { 
var G__34170__delegate = function (args){
cljs.core.apply.call(null,figwheel.client.file_reloading.addDependency,args);

return cljs.core.apply.call(null,goog.addDependency_figwheel_backup_,args);
};
var G__34170 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__34171__i = 0, G__34171__a = new Array(arguments.length -  0);
while (G__34171__i < G__34171__a.length) {G__34171__a[G__34171__i] = arguments[G__34171__i + 0]; ++G__34171__i;}
  args = new cljs.core.IndexedSeq(G__34171__a,0);
} 
return G__34170__delegate.call(this,args);};
G__34170.cljs$lang$maxFixedArity = 0;
G__34170.cljs$lang$applyTo = (function (arglist__34172){
var args = cljs.core.seq(arglist__34172);
return G__34170__delegate(args);
});
G__34170.cljs$core$IFn$_invoke$arity$variadic = G__34170__delegate;
return G__34170;
})()
;

goog.constructNamespace_("cljs.user");

goog.global.CLOSURE_IMPORT_SCRIPT = figwheel.client.file_reloading.queued_file_reload;

return goog.require = figwheel.client.file_reloading.figwheel_require;
}
});
figwheel.client.file_reloading.patch_goog_base = (function figwheel$client$file_reloading$patch_goog_base(){
if(typeof figwheel.client.file_reloading.bootstrapped_cljs !== 'undefined'){
return null;
} else {
figwheel.client.file_reloading.bootstrapped_cljs = (function (){
figwheel.client.file_reloading.bootstrap_goog_base.call(null);

return true;
})()
;
}
});
figwheel.client.file_reloading.reload_file_STAR_ = (function (){var pred__34174 = cljs.core._EQ_;
var expr__34175 = figwheel.client.utils.host_env_QMARK_.call(null);
if(cljs.core.truth_(pred__34174.call(null,new cljs.core.Keyword(null,"node","node",581201198),expr__34175))){
var path_parts = ((function (pred__34174,expr__34175){
return (function (p1__34173_SHARP_){
return clojure.string.split.call(null,p1__34173_SHARP_,/[\/\\]/);
});})(pred__34174,expr__34175))
;
var sep = (cljs.core.truth_(cljs.core.re_matches.call(null,/win.*/,process.platform))?"\\":"/");
var root = clojure.string.join.call(null,sep,cljs.core.pop.call(null,cljs.core.pop.call(null,path_parts.call(null,__dirname))));
return ((function (path_parts,sep,root,pred__34174,expr__34175){
return (function (request_url,callback){

var cache_path = clojure.string.join.call(null,sep,cljs.core.cons.call(null,root,path_parts.call(null,figwheel.client.file_reloading.fix_node_request_url.call(null,request_url))));
(require.cache[cache_path] = null);

return callback.call(null,(function (){try{return require(cache_path);
}catch (e34177){if((e34177 instanceof Error)){
var e = e34177;
figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"error","error",-978969032),[cljs.core.str("Figwheel: Error loading file "),cljs.core.str(cache_path)].join(''));

figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"error","error",-978969032),e.stack);

return false;
} else {
throw e34177;

}
}})());
});
;})(path_parts,sep,root,pred__34174,expr__34175))
} else {
if(cljs.core.truth_(pred__34174.call(null,new cljs.core.Keyword(null,"html","html",-998796897),expr__34175))){
return ((function (pred__34174,expr__34175){
return (function (request_url,callback){

var deferred = goog.net.jsloader.load(figwheel.client.file_reloading.add_cache_buster.call(null,request_url),({"cleanupWhenDone": true}));
deferred.addCallback(((function (deferred,pred__34174,expr__34175){
return (function (){
return cljs.core.apply.call(null,callback,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [true], null));
});})(deferred,pred__34174,expr__34175))
);

return deferred.addErrback(((function (deferred,pred__34174,expr__34175){
return (function (){
return cljs.core.apply.call(null,callback,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [false], null));
});})(deferred,pred__34174,expr__34175))
);
});
;})(pred__34174,expr__34175))
} else {
return ((function (pred__34174,expr__34175){
return (function (a,b){
throw "Reload not defined for this platform";
});
;})(pred__34174,expr__34175))
}
}
})();
figwheel.client.file_reloading.reload_file = (function figwheel$client$file_reloading$reload_file(p__34178,callback){
var map__34181 = p__34178;
var map__34181__$1 = ((((!((map__34181 == null)))?((((map__34181.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34181.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34181):map__34181);
var file_msg = map__34181__$1;
var request_url = cljs.core.get.call(null,map__34181__$1,new cljs.core.Keyword(null,"request-url","request-url",2100346596));

figwheel.client.utils.debug_prn.call(null,[cljs.core.str("FigWheel: Attempting to load "),cljs.core.str(request_url)].join(''));

return figwheel.client.file_reloading.reload_file_STAR_.call(null,request_url,((function (map__34181,map__34181__$1,file_msg,request_url){
return (function (success_QMARK_){
if(cljs.core.truth_(success_QMARK_)){
figwheel.client.utils.debug_prn.call(null,[cljs.core.str("FigWheel: Successfully loaded "),cljs.core.str(request_url)].join(''));

return cljs.core.apply.call(null,callback,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.assoc.call(null,file_msg,new cljs.core.Keyword(null,"loaded-file","loaded-file",-168399375),true)], null));
} else {
figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"error","error",-978969032),[cljs.core.str("Figwheel: Error loading file "),cljs.core.str(request_url)].join(''));

return cljs.core.apply.call(null,callback,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [file_msg], null));
}
});})(map__34181,map__34181__$1,file_msg,request_url))
);
});
if(typeof figwheel.client.file_reloading.reload_chan !== 'undefined'){
} else {
figwheel.client.file_reloading.reload_chan = cljs.core.async.chan.call(null);
}
if(typeof figwheel.client.file_reloading.on_load_callbacks !== 'undefined'){
} else {
figwheel.client.file_reloading.on_load_callbacks = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
}
if(typeof figwheel.client.file_reloading.dependencies_loaded !== 'undefined'){
} else {
figwheel.client.file_reloading.dependencies_loaded = cljs.core.atom.call(null,cljs.core.PersistentVector.EMPTY);
}
figwheel.client.file_reloading.blocking_load = (function figwheel$client$file_reloading$blocking_load(url){
var out = cljs.core.async.chan.call(null);
figwheel.client.file_reloading.reload_file.call(null,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"request-url","request-url",2100346596),url], null),((function (out){
return (function (file_msg){
cljs.core.async.put_BANG_.call(null,out,file_msg);

return cljs.core.async.close_BANG_.call(null,out);
});})(out))
);

return out;
});
if(typeof figwheel.client.file_reloading.reloader_loop !== 'undefined'){
} else {
figwheel.client.file_reloading.reloader_loop = (function (){var c__29520__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto__){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto__){
return (function (state_34205){
var state_val_34206 = (state_34205[(1)]);
if((state_val_34206 === (7))){
var inst_34201 = (state_34205[(2)]);
var state_34205__$1 = state_34205;
var statearr_34207_34227 = state_34205__$1;
(statearr_34207_34227[(2)] = inst_34201);

(statearr_34207_34227[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34206 === (1))){
var state_34205__$1 = state_34205;
var statearr_34208_34228 = state_34205__$1;
(statearr_34208_34228[(2)] = null);

(statearr_34208_34228[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34206 === (4))){
var inst_34185 = (state_34205[(7)]);
var inst_34185__$1 = (state_34205[(2)]);
var state_34205__$1 = (function (){var statearr_34209 = state_34205;
(statearr_34209[(7)] = inst_34185__$1);

return statearr_34209;
})();
if(cljs.core.truth_(inst_34185__$1)){
var statearr_34210_34229 = state_34205__$1;
(statearr_34210_34229[(1)] = (5));

} else {
var statearr_34211_34230 = state_34205__$1;
(statearr_34211_34230[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34206 === (6))){
var state_34205__$1 = state_34205;
var statearr_34212_34231 = state_34205__$1;
(statearr_34212_34231[(2)] = null);

(statearr_34212_34231[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34206 === (3))){
var inst_34203 = (state_34205[(2)]);
var state_34205__$1 = state_34205;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_34205__$1,inst_34203);
} else {
if((state_val_34206 === (2))){
var state_34205__$1 = state_34205;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_34205__$1,(4),figwheel.client.file_reloading.reload_chan);
} else {
if((state_val_34206 === (11))){
var inst_34197 = (state_34205[(2)]);
var state_34205__$1 = (function (){var statearr_34213 = state_34205;
(statearr_34213[(8)] = inst_34197);

return statearr_34213;
})();
var statearr_34214_34232 = state_34205__$1;
(statearr_34214_34232[(2)] = null);

(statearr_34214_34232[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34206 === (9))){
var inst_34189 = (state_34205[(9)]);
var inst_34191 = (state_34205[(10)]);
var inst_34193 = inst_34191.call(null,inst_34189);
var state_34205__$1 = state_34205;
var statearr_34215_34233 = state_34205__$1;
(statearr_34215_34233[(2)] = inst_34193);

(statearr_34215_34233[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34206 === (5))){
var inst_34185 = (state_34205[(7)]);
var inst_34187 = figwheel.client.file_reloading.blocking_load.call(null,inst_34185);
var state_34205__$1 = state_34205;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_34205__$1,(8),inst_34187);
} else {
if((state_val_34206 === (10))){
var inst_34189 = (state_34205[(9)]);
var inst_34195 = cljs.core.swap_BANG_.call(null,figwheel.client.file_reloading.dependencies_loaded,cljs.core.conj,inst_34189);
var state_34205__$1 = state_34205;
var statearr_34216_34234 = state_34205__$1;
(statearr_34216_34234[(2)] = inst_34195);

(statearr_34216_34234[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34206 === (8))){
var inst_34185 = (state_34205[(7)]);
var inst_34191 = (state_34205[(10)]);
var inst_34189 = (state_34205[(2)]);
var inst_34190 = cljs.core.deref.call(null,figwheel.client.file_reloading.on_load_callbacks);
var inst_34191__$1 = cljs.core.get.call(null,inst_34190,inst_34185);
var state_34205__$1 = (function (){var statearr_34217 = state_34205;
(statearr_34217[(9)] = inst_34189);

(statearr_34217[(10)] = inst_34191__$1);

return statearr_34217;
})();
if(cljs.core.truth_(inst_34191__$1)){
var statearr_34218_34235 = state_34205__$1;
(statearr_34218_34235[(1)] = (9));

} else {
var statearr_34219_34236 = state_34205__$1;
(statearr_34219_34236[(1)] = (10));

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
});})(c__29520__auto__))
;
return ((function (switch__29408__auto__,c__29520__auto__){
return (function() {
var figwheel$client$file_reloading$state_machine__29409__auto__ = null;
var figwheel$client$file_reloading$state_machine__29409__auto____0 = (function (){
var statearr_34223 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_34223[(0)] = figwheel$client$file_reloading$state_machine__29409__auto__);

(statearr_34223[(1)] = (1));

return statearr_34223;
});
var figwheel$client$file_reloading$state_machine__29409__auto____1 = (function (state_34205){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_34205);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e34224){if((e34224 instanceof Object)){
var ex__29412__auto__ = e34224;
var statearr_34225_34237 = state_34205;
(statearr_34225_34237[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_34205);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e34224;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34238 = state_34205;
state_34205 = G__34238;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
figwheel$client$file_reloading$state_machine__29409__auto__ = function(state_34205){
switch(arguments.length){
case 0:
return figwheel$client$file_reloading$state_machine__29409__auto____0.call(this);
case 1:
return figwheel$client$file_reloading$state_machine__29409__auto____1.call(this,state_34205);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
figwheel$client$file_reloading$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = figwheel$client$file_reloading$state_machine__29409__auto____0;
figwheel$client$file_reloading$state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = figwheel$client$file_reloading$state_machine__29409__auto____1;
return figwheel$client$file_reloading$state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto__))
})();
var state__29522__auto__ = (function (){var statearr_34226 = f__29521__auto__.call(null);
(statearr_34226[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto__);

return statearr_34226;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto__))
);

return c__29520__auto__;
})();
}
figwheel.client.file_reloading.queued_file_reload = (function figwheel$client$file_reloading$queued_file_reload(url){
return cljs.core.async.put_BANG_.call(null,figwheel.client.file_reloading.reload_chan,url);
});
figwheel.client.file_reloading.require_with_callback = (function figwheel$client$file_reloading$require_with_callback(p__34239,callback){
var map__34242 = p__34239;
var map__34242__$1 = ((((!((map__34242 == null)))?((((map__34242.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34242.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34242):map__34242);
var file_msg = map__34242__$1;
var namespace = cljs.core.get.call(null,map__34242__$1,new cljs.core.Keyword(null,"namespace","namespace",-377510372));
var request_url = figwheel.client.file_reloading.resolve_ns.call(null,namespace);
cljs.core.swap_BANG_.call(null,figwheel.client.file_reloading.on_load_callbacks,cljs.core.assoc,request_url,((function (request_url,map__34242,map__34242__$1,file_msg,namespace){
return (function (file_msg_SINGLEQUOTE_){
cljs.core.swap_BANG_.call(null,figwheel.client.file_reloading.on_load_callbacks,cljs.core.dissoc,request_url);

return cljs.core.apply.call(null,callback,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.merge.call(null,file_msg,cljs.core.select_keys.call(null,file_msg_SINGLEQUOTE_,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"loaded-file","loaded-file",-168399375)], null)))], null));
});})(request_url,map__34242,map__34242__$1,file_msg,namespace))
);

return figwheel.client.file_reloading.figwheel_require.call(null,cljs.core.name.call(null,namespace),true);
});
figwheel.client.file_reloading.reload_file_QMARK_ = (function figwheel$client$file_reloading$reload_file_QMARK_(p__34244){
var map__34247 = p__34244;
var map__34247__$1 = ((((!((map__34247 == null)))?((((map__34247.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34247.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34247):map__34247);
var file_msg = map__34247__$1;
var namespace = cljs.core.get.call(null,map__34247__$1,new cljs.core.Keyword(null,"namespace","namespace",-377510372));

var meta_pragmas = cljs.core.get.call(null,cljs.core.deref.call(null,figwheel.client.file_reloading.figwheel_meta_pragmas),cljs.core.name.call(null,namespace));
var and__24778__auto__ = cljs.core.not.call(null,new cljs.core.Keyword(null,"figwheel-no-load","figwheel-no-load",-555840179).cljs$core$IFn$_invoke$arity$1(meta_pragmas));
if(and__24778__auto__){
var or__24790__auto__ = new cljs.core.Keyword(null,"figwheel-always","figwheel-always",799819691).cljs$core$IFn$_invoke$arity$1(meta_pragmas);
if(cljs.core.truth_(or__24790__auto__)){
return or__24790__auto__;
} else {
var or__24790__auto____$1 = new cljs.core.Keyword(null,"figwheel-load","figwheel-load",1316089175).cljs$core$IFn$_invoke$arity$1(meta_pragmas);
if(cljs.core.truth_(or__24790__auto____$1)){
return or__24790__auto____$1;
} else {
return figwheel.client.file_reloading.provided_QMARK_.call(null,cljs.core.name.call(null,namespace));
}
}
} else {
return and__24778__auto__;
}
});
figwheel.client.file_reloading.js_reload = (function figwheel$client$file_reloading$js_reload(p__34249,callback){
var map__34252 = p__34249;
var map__34252__$1 = ((((!((map__34252 == null)))?((((map__34252.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34252.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34252):map__34252);
var file_msg = map__34252__$1;
var request_url = cljs.core.get.call(null,map__34252__$1,new cljs.core.Keyword(null,"request-url","request-url",2100346596));
var namespace = cljs.core.get.call(null,map__34252__$1,new cljs.core.Keyword(null,"namespace","namespace",-377510372));

if(cljs.core.truth_(figwheel.client.file_reloading.reload_file_QMARK_.call(null,file_msg))){
return figwheel.client.file_reloading.require_with_callback.call(null,file_msg,callback);
} else {
figwheel.client.utils.debug_prn.call(null,[cljs.core.str("Figwheel: Not trying to load file "),cljs.core.str(request_url)].join(''));

return cljs.core.apply.call(null,callback,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [file_msg], null));
}
});
figwheel.client.file_reloading.reload_js_file = (function figwheel$client$file_reloading$reload_js_file(file_msg){
var out = cljs.core.async.chan.call(null);
figwheel.client.file_reloading.js_reload.call(null,file_msg,((function (out){
return (function (url){
cljs.core.async.put_BANG_.call(null,out,url);

return cljs.core.async.close_BANG_.call(null,out);
});})(out))
);

return out;
});
/**
 * Returns a chanel with one collection of loaded filenames on it.
 */
figwheel.client.file_reloading.load_all_js_files = (function figwheel$client$file_reloading$load_all_js_files(files){
var out = cljs.core.async.chan.call(null);
var c__29520__auto___34356 = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto___34356,out){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto___34356,out){
return (function (state_34338){
var state_val_34339 = (state_34338[(1)]);
if((state_val_34339 === (1))){
var inst_34312 = cljs.core.seq.call(null,files);
var inst_34313 = cljs.core.first.call(null,inst_34312);
var inst_34314 = cljs.core.next.call(null,inst_34312);
var inst_34315 = files;
var state_34338__$1 = (function (){var statearr_34340 = state_34338;
(statearr_34340[(7)] = inst_34315);

(statearr_34340[(8)] = inst_34313);

(statearr_34340[(9)] = inst_34314);

return statearr_34340;
})();
var statearr_34341_34357 = state_34338__$1;
(statearr_34341_34357[(2)] = null);

(statearr_34341_34357[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34339 === (2))){
var inst_34315 = (state_34338[(7)]);
var inst_34321 = (state_34338[(10)]);
var inst_34320 = cljs.core.seq.call(null,inst_34315);
var inst_34321__$1 = cljs.core.first.call(null,inst_34320);
var inst_34322 = cljs.core.next.call(null,inst_34320);
var inst_34323 = (inst_34321__$1 == null);
var inst_34324 = cljs.core.not.call(null,inst_34323);
var state_34338__$1 = (function (){var statearr_34342 = state_34338;
(statearr_34342[(10)] = inst_34321__$1);

(statearr_34342[(11)] = inst_34322);

return statearr_34342;
})();
if(inst_34324){
var statearr_34343_34358 = state_34338__$1;
(statearr_34343_34358[(1)] = (4));

} else {
var statearr_34344_34359 = state_34338__$1;
(statearr_34344_34359[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34339 === (3))){
var inst_34336 = (state_34338[(2)]);
var state_34338__$1 = state_34338;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_34338__$1,inst_34336);
} else {
if((state_val_34339 === (4))){
var inst_34321 = (state_34338[(10)]);
var inst_34326 = figwheel.client.file_reloading.reload_js_file.call(null,inst_34321);
var state_34338__$1 = state_34338;
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_34338__$1,(7),inst_34326);
} else {
if((state_val_34339 === (5))){
var inst_34332 = cljs.core.async.close_BANG_.call(null,out);
var state_34338__$1 = state_34338;
var statearr_34345_34360 = state_34338__$1;
(statearr_34345_34360[(2)] = inst_34332);

(statearr_34345_34360[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34339 === (6))){
var inst_34334 = (state_34338[(2)]);
var state_34338__$1 = state_34338;
var statearr_34346_34361 = state_34338__$1;
(statearr_34346_34361[(2)] = inst_34334);

(statearr_34346_34361[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34339 === (7))){
var inst_34322 = (state_34338[(11)]);
var inst_34328 = (state_34338[(2)]);
var inst_34329 = cljs.core.async.put_BANG_.call(null,out,inst_34328);
var inst_34315 = inst_34322;
var state_34338__$1 = (function (){var statearr_34347 = state_34338;
(statearr_34347[(7)] = inst_34315);

(statearr_34347[(12)] = inst_34329);

return statearr_34347;
})();
var statearr_34348_34362 = state_34338__$1;
(statearr_34348_34362[(2)] = null);

(statearr_34348_34362[(1)] = (2));


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
});})(c__29520__auto___34356,out))
;
return ((function (switch__29408__auto__,c__29520__auto___34356,out){
return (function() {
var figwheel$client$file_reloading$load_all_js_files_$_state_machine__29409__auto__ = null;
var figwheel$client$file_reloading$load_all_js_files_$_state_machine__29409__auto____0 = (function (){
var statearr_34352 = [null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_34352[(0)] = figwheel$client$file_reloading$load_all_js_files_$_state_machine__29409__auto__);

(statearr_34352[(1)] = (1));

return statearr_34352;
});
var figwheel$client$file_reloading$load_all_js_files_$_state_machine__29409__auto____1 = (function (state_34338){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_34338);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e34353){if((e34353 instanceof Object)){
var ex__29412__auto__ = e34353;
var statearr_34354_34363 = state_34338;
(statearr_34354_34363[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_34338);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e34353;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34364 = state_34338;
state_34338 = G__34364;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
figwheel$client$file_reloading$load_all_js_files_$_state_machine__29409__auto__ = function(state_34338){
switch(arguments.length){
case 0:
return figwheel$client$file_reloading$load_all_js_files_$_state_machine__29409__auto____0.call(this);
case 1:
return figwheel$client$file_reloading$load_all_js_files_$_state_machine__29409__auto____1.call(this,state_34338);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
figwheel$client$file_reloading$load_all_js_files_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = figwheel$client$file_reloading$load_all_js_files_$_state_machine__29409__auto____0;
figwheel$client$file_reloading$load_all_js_files_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = figwheel$client$file_reloading$load_all_js_files_$_state_machine__29409__auto____1;
return figwheel$client$file_reloading$load_all_js_files_$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto___34356,out))
})();
var state__29522__auto__ = (function (){var statearr_34355 = f__29521__auto__.call(null);
(statearr_34355[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto___34356);

return statearr_34355;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto___34356,out))
);


return cljs.core.async.into.call(null,cljs.core.PersistentVector.EMPTY,out);
});
figwheel.client.file_reloading.eval_body = (function figwheel$client$file_reloading$eval_body(p__34365,opts){
var map__34369 = p__34365;
var map__34369__$1 = ((((!((map__34369 == null)))?((((map__34369.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34369.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34369):map__34369);
var eval_body__$1 = cljs.core.get.call(null,map__34369__$1,new cljs.core.Keyword(null,"eval-body","eval-body",-907279883));
var file = cljs.core.get.call(null,map__34369__$1,new cljs.core.Keyword(null,"file","file",-1269645878));
if(cljs.core.truth_((function (){var and__24778__auto__ = eval_body__$1;
if(cljs.core.truth_(and__24778__auto__)){
return typeof eval_body__$1 === 'string';
} else {
return and__24778__auto__;
}
})())){
var code = eval_body__$1;
try{figwheel.client.utils.debug_prn.call(null,[cljs.core.str("Evaling file "),cljs.core.str(file)].join(''));

return figwheel.client.utils.eval_helper.call(null,code,opts);
}catch (e34371){var e = e34371;
return figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"error","error",-978969032),[cljs.core.str("Unable to evaluate "),cljs.core.str(file)].join(''));
}} else {
return null;
}
});
figwheel.client.file_reloading.expand_files = (function figwheel$client$file_reloading$expand_files(files){
var deps = figwheel.client.file_reloading.get_all_dependents.call(null,cljs.core.map.call(null,new cljs.core.Keyword(null,"namespace","namespace",-377510372),files));
return cljs.core.filter.call(null,cljs.core.comp.call(null,cljs.core.not,new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 1, ["figwheel.connect",null], null), null),new cljs.core.Keyword(null,"namespace","namespace",-377510372)),cljs.core.map.call(null,((function (deps){
return (function (n){
var temp__4655__auto__ = cljs.core.first.call(null,cljs.core.filter.call(null,((function (deps){
return (function (p1__34372_SHARP_){
return cljs.core._EQ_.call(null,new cljs.core.Keyword(null,"namespace","namespace",-377510372).cljs$core$IFn$_invoke$arity$1(p1__34372_SHARP_),n);
});})(deps))
,files));
if(cljs.core.truth_(temp__4655__auto__)){
var file_msg = temp__4655__auto__;
return file_msg;
} else {
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"namespace","namespace",-377510372),new cljs.core.Keyword(null,"namespace","namespace",-377510372),n], null);
}
});})(deps))
,deps));
});
figwheel.client.file_reloading.sort_files = (function figwheel$client$file_reloading$sort_files(files){
if((cljs.core.count.call(null,files) <= (1))){
return files;
} else {
var keep_files = cljs.core.set.call(null,cljs.core.keep.call(null,new cljs.core.Keyword(null,"namespace","namespace",-377510372),files));
return cljs.core.filter.call(null,cljs.core.comp.call(null,keep_files,new cljs.core.Keyword(null,"namespace","namespace",-377510372)),figwheel.client.file_reloading.expand_files.call(null,files));
}
});
figwheel.client.file_reloading.get_figwheel_always = (function figwheel$client$file_reloading$get_figwheel_always(){
return cljs.core.map.call(null,(function (p__34381){
var vec__34382 = p__34381;
var k = cljs.core.nth.call(null,vec__34382,(0),null);
var v = cljs.core.nth.call(null,vec__34382,(1),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"namespace","namespace",-377510372),k,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"namespace","namespace",-377510372)], null);
}),cljs.core.filter.call(null,(function (p__34385){
var vec__34386 = p__34385;
var k = cljs.core.nth.call(null,vec__34386,(0),null);
var v = cljs.core.nth.call(null,vec__34386,(1),null);
return new cljs.core.Keyword(null,"figwheel-always","figwheel-always",799819691).cljs$core$IFn$_invoke$arity$1(v);
}),cljs.core.deref.call(null,figwheel.client.file_reloading.figwheel_meta_pragmas)));
});
figwheel.client.file_reloading.reload_js_files = (function figwheel$client$file_reloading$reload_js_files(p__34392,p__34393){
var map__34640 = p__34392;
var map__34640__$1 = ((((!((map__34640 == null)))?((((map__34640.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34640.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34640):map__34640);
var opts = map__34640__$1;
var before_jsload = cljs.core.get.call(null,map__34640__$1,new cljs.core.Keyword(null,"before-jsload","before-jsload",-847513128));
var on_jsload = cljs.core.get.call(null,map__34640__$1,new cljs.core.Keyword(null,"on-jsload","on-jsload",-395756602));
var reload_dependents = cljs.core.get.call(null,map__34640__$1,new cljs.core.Keyword(null,"reload-dependents","reload-dependents",-956865430));
var map__34641 = p__34393;
var map__34641__$1 = ((((!((map__34641 == null)))?((((map__34641.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34641.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34641):map__34641);
var msg = map__34641__$1;
var files = cljs.core.get.call(null,map__34641__$1,new cljs.core.Keyword(null,"files","files",-472457450));
var figwheel_meta = cljs.core.get.call(null,map__34641__$1,new cljs.core.Keyword(null,"figwheel-meta","figwheel-meta",-225970237));
var recompile_dependents = cljs.core.get.call(null,map__34641__$1,new cljs.core.Keyword(null,"recompile-dependents","recompile-dependents",523804171));
if(cljs.core.empty_QMARK_.call(null,figwheel_meta)){
} else {
cljs.core.reset_BANG_.call(null,figwheel.client.file_reloading.figwheel_meta_pragmas,figwheel_meta);
}

var c__29520__auto__ = cljs.core.async.chan.call(null,(1));
cljs.core.async.impl.dispatch.run.call(null,((function (c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents){
return (function (){
var f__29521__auto__ = (function (){var switch__29408__auto__ = ((function (c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents){
return (function (state_34794){
var state_val_34795 = (state_34794[(1)]);
if((state_val_34795 === (7))){
var inst_34657 = (state_34794[(7)]);
var inst_34655 = (state_34794[(8)]);
var inst_34658 = (state_34794[(9)]);
var inst_34656 = (state_34794[(10)]);
var inst_34663 = cljs.core._nth.call(null,inst_34656,inst_34658);
var inst_34664 = figwheel.client.file_reloading.eval_body.call(null,inst_34663,opts);
var inst_34665 = (inst_34658 + (1));
var tmp34796 = inst_34657;
var tmp34797 = inst_34655;
var tmp34798 = inst_34656;
var inst_34655__$1 = tmp34797;
var inst_34656__$1 = tmp34798;
var inst_34657__$1 = tmp34796;
var inst_34658__$1 = inst_34665;
var state_34794__$1 = (function (){var statearr_34799 = state_34794;
(statearr_34799[(7)] = inst_34657__$1);

(statearr_34799[(8)] = inst_34655__$1);

(statearr_34799[(9)] = inst_34658__$1);

(statearr_34799[(11)] = inst_34664);

(statearr_34799[(10)] = inst_34656__$1);

return statearr_34799;
})();
var statearr_34800_34886 = state_34794__$1;
(statearr_34800_34886[(2)] = null);

(statearr_34800_34886[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (20))){
var inst_34698 = (state_34794[(12)]);
var inst_34706 = figwheel.client.file_reloading.sort_files.call(null,inst_34698);
var state_34794__$1 = state_34794;
var statearr_34801_34887 = state_34794__$1;
(statearr_34801_34887[(2)] = inst_34706);

(statearr_34801_34887[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (27))){
var state_34794__$1 = state_34794;
var statearr_34802_34888 = state_34794__$1;
(statearr_34802_34888[(2)] = null);

(statearr_34802_34888[(1)] = (28));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (1))){
var inst_34647 = (state_34794[(13)]);
var inst_34644 = before_jsload.call(null,files);
var inst_34645 = figwheel.client.file_reloading.before_jsload_custom_event.call(null,files);
var inst_34646 = (function (){return ((function (inst_34647,inst_34644,inst_34645,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents){
return (function (p1__34389_SHARP_){
return new cljs.core.Keyword(null,"eval-body","eval-body",-907279883).cljs$core$IFn$_invoke$arity$1(p1__34389_SHARP_);
});
;})(inst_34647,inst_34644,inst_34645,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents))
})();
var inst_34647__$1 = cljs.core.filter.call(null,inst_34646,files);
var inst_34648 = cljs.core.not_empty.call(null,inst_34647__$1);
var state_34794__$1 = (function (){var statearr_34803 = state_34794;
(statearr_34803[(13)] = inst_34647__$1);

(statearr_34803[(14)] = inst_34644);

(statearr_34803[(15)] = inst_34645);

return statearr_34803;
})();
if(cljs.core.truth_(inst_34648)){
var statearr_34804_34889 = state_34794__$1;
(statearr_34804_34889[(1)] = (2));

} else {
var statearr_34805_34890 = state_34794__$1;
(statearr_34805_34890[(1)] = (3));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (24))){
var state_34794__$1 = state_34794;
var statearr_34806_34891 = state_34794__$1;
(statearr_34806_34891[(2)] = null);

(statearr_34806_34891[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (39))){
var inst_34748 = (state_34794[(16)]);
var state_34794__$1 = state_34794;
var statearr_34807_34892 = state_34794__$1;
(statearr_34807_34892[(2)] = inst_34748);

(statearr_34807_34892[(1)] = (40));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (46))){
var inst_34789 = (state_34794[(2)]);
var state_34794__$1 = state_34794;
var statearr_34808_34893 = state_34794__$1;
(statearr_34808_34893[(2)] = inst_34789);

(statearr_34808_34893[(1)] = (31));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (4))){
var inst_34692 = (state_34794[(2)]);
var inst_34693 = cljs.core.List.EMPTY;
var inst_34694 = cljs.core.reset_BANG_.call(null,figwheel.client.file_reloading.dependencies_loaded,inst_34693);
var inst_34695 = (function (){return ((function (inst_34692,inst_34693,inst_34694,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents){
return (function (p1__34390_SHARP_){
var and__24778__auto__ = new cljs.core.Keyword(null,"namespace","namespace",-377510372).cljs$core$IFn$_invoke$arity$1(p1__34390_SHARP_);
if(cljs.core.truth_(and__24778__auto__)){
return cljs.core.not.call(null,new cljs.core.Keyword(null,"eval-body","eval-body",-907279883).cljs$core$IFn$_invoke$arity$1(p1__34390_SHARP_));
} else {
return and__24778__auto__;
}
});
;})(inst_34692,inst_34693,inst_34694,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents))
})();
var inst_34696 = cljs.core.filter.call(null,inst_34695,files);
var inst_34697 = figwheel.client.file_reloading.get_figwheel_always.call(null);
var inst_34698 = cljs.core.concat.call(null,inst_34696,inst_34697);
var state_34794__$1 = (function (){var statearr_34809 = state_34794;
(statearr_34809[(12)] = inst_34698);

(statearr_34809[(17)] = inst_34694);

(statearr_34809[(18)] = inst_34692);

return statearr_34809;
})();
if(cljs.core.truth_(reload_dependents)){
var statearr_34810_34894 = state_34794__$1;
(statearr_34810_34894[(1)] = (16));

} else {
var statearr_34811_34895 = state_34794__$1;
(statearr_34811_34895[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (15))){
var inst_34682 = (state_34794[(2)]);
var state_34794__$1 = state_34794;
var statearr_34812_34896 = state_34794__$1;
(statearr_34812_34896[(2)] = inst_34682);

(statearr_34812_34896[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (21))){
var inst_34708 = (state_34794[(19)]);
var inst_34708__$1 = (state_34794[(2)]);
var inst_34709 = figwheel.client.file_reloading.load_all_js_files.call(null,inst_34708__$1);
var state_34794__$1 = (function (){var statearr_34813 = state_34794;
(statearr_34813[(19)] = inst_34708__$1);

return statearr_34813;
})();
return cljs.core.async.impl.ioc_helpers.take_BANG_.call(null,state_34794__$1,(22),inst_34709);
} else {
if((state_val_34795 === (31))){
var inst_34792 = (state_34794[(2)]);
var state_34794__$1 = state_34794;
return cljs.core.async.impl.ioc_helpers.return_chan.call(null,state_34794__$1,inst_34792);
} else {
if((state_val_34795 === (32))){
var inst_34748 = (state_34794[(16)]);
var inst_34753 = inst_34748.cljs$lang$protocol_mask$partition0$;
var inst_34754 = (inst_34753 & (64));
var inst_34755 = inst_34748.cljs$core$ISeq$;
var inst_34756 = (inst_34754) || (inst_34755);
var state_34794__$1 = state_34794;
if(cljs.core.truth_(inst_34756)){
var statearr_34814_34897 = state_34794__$1;
(statearr_34814_34897[(1)] = (35));

} else {
var statearr_34815_34898 = state_34794__$1;
(statearr_34815_34898[(1)] = (36));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (40))){
var inst_34769 = (state_34794[(20)]);
var inst_34768 = (state_34794[(2)]);
var inst_34769__$1 = cljs.core.get.call(null,inst_34768,new cljs.core.Keyword(null,"figwheel-no-load","figwheel-no-load",-555840179));
var inst_34770 = cljs.core.get.call(null,inst_34768,new cljs.core.Keyword(null,"not-required","not-required",-950359114));
var inst_34771 = cljs.core.not_empty.call(null,inst_34769__$1);
var state_34794__$1 = (function (){var statearr_34816 = state_34794;
(statearr_34816[(20)] = inst_34769__$1);

(statearr_34816[(21)] = inst_34770);

return statearr_34816;
})();
if(cljs.core.truth_(inst_34771)){
var statearr_34817_34899 = state_34794__$1;
(statearr_34817_34899[(1)] = (41));

} else {
var statearr_34818_34900 = state_34794__$1;
(statearr_34818_34900[(1)] = (42));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (33))){
var state_34794__$1 = state_34794;
var statearr_34819_34901 = state_34794__$1;
(statearr_34819_34901[(2)] = false);

(statearr_34819_34901[(1)] = (34));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (13))){
var inst_34668 = (state_34794[(22)]);
var inst_34672 = cljs.core.chunk_first.call(null,inst_34668);
var inst_34673 = cljs.core.chunk_rest.call(null,inst_34668);
var inst_34674 = cljs.core.count.call(null,inst_34672);
var inst_34655 = inst_34673;
var inst_34656 = inst_34672;
var inst_34657 = inst_34674;
var inst_34658 = (0);
var state_34794__$1 = (function (){var statearr_34820 = state_34794;
(statearr_34820[(7)] = inst_34657);

(statearr_34820[(8)] = inst_34655);

(statearr_34820[(9)] = inst_34658);

(statearr_34820[(10)] = inst_34656);

return statearr_34820;
})();
var statearr_34821_34902 = state_34794__$1;
(statearr_34821_34902[(2)] = null);

(statearr_34821_34902[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (22))){
var inst_34711 = (state_34794[(23)]);
var inst_34716 = (state_34794[(24)]);
var inst_34712 = (state_34794[(25)]);
var inst_34708 = (state_34794[(19)]);
var inst_34711__$1 = (state_34794[(2)]);
var inst_34712__$1 = cljs.core.filter.call(null,new cljs.core.Keyword(null,"loaded-file","loaded-file",-168399375),inst_34711__$1);
var inst_34713 = (function (){var all_files = inst_34708;
var res_SINGLEQUOTE_ = inst_34711__$1;
var res = inst_34712__$1;
return ((function (all_files,res_SINGLEQUOTE_,res,inst_34711,inst_34716,inst_34712,inst_34708,inst_34711__$1,inst_34712__$1,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents){
return (function (p1__34391_SHARP_){
return cljs.core.not.call(null,new cljs.core.Keyword(null,"loaded-file","loaded-file",-168399375).cljs$core$IFn$_invoke$arity$1(p1__34391_SHARP_));
});
;})(all_files,res_SINGLEQUOTE_,res,inst_34711,inst_34716,inst_34712,inst_34708,inst_34711__$1,inst_34712__$1,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents))
})();
var inst_34714 = cljs.core.filter.call(null,inst_34713,inst_34711__$1);
var inst_34715 = cljs.core.deref.call(null,figwheel.client.file_reloading.dependencies_loaded);
var inst_34716__$1 = cljs.core.filter.call(null,new cljs.core.Keyword(null,"loaded-file","loaded-file",-168399375),inst_34715);
var inst_34717 = cljs.core.not_empty.call(null,inst_34716__$1);
var state_34794__$1 = (function (){var statearr_34822 = state_34794;
(statearr_34822[(23)] = inst_34711__$1);

(statearr_34822[(24)] = inst_34716__$1);

(statearr_34822[(25)] = inst_34712__$1);

(statearr_34822[(26)] = inst_34714);

return statearr_34822;
})();
if(cljs.core.truth_(inst_34717)){
var statearr_34823_34903 = state_34794__$1;
(statearr_34823_34903[(1)] = (23));

} else {
var statearr_34824_34904 = state_34794__$1;
(statearr_34824_34904[(1)] = (24));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (36))){
var state_34794__$1 = state_34794;
var statearr_34825_34905 = state_34794__$1;
(statearr_34825_34905[(2)] = false);

(statearr_34825_34905[(1)] = (37));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (41))){
var inst_34769 = (state_34794[(20)]);
var inst_34773 = cljs.core.comp.call(null,figwheel.client.file_reloading.name__GT_path,new cljs.core.Keyword(null,"namespace","namespace",-377510372));
var inst_34774 = cljs.core.map.call(null,inst_34773,inst_34769);
var inst_34775 = cljs.core.pr_str.call(null,inst_34774);
var inst_34776 = [cljs.core.str("figwheel-no-load meta-data: "),cljs.core.str(inst_34775)].join('');
var inst_34777 = figwheel.client.utils.log.call(null,inst_34776);
var state_34794__$1 = state_34794;
var statearr_34826_34906 = state_34794__$1;
(statearr_34826_34906[(2)] = inst_34777);

(statearr_34826_34906[(1)] = (43));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (43))){
var inst_34770 = (state_34794[(21)]);
var inst_34780 = (state_34794[(2)]);
var inst_34781 = cljs.core.not_empty.call(null,inst_34770);
var state_34794__$1 = (function (){var statearr_34827 = state_34794;
(statearr_34827[(27)] = inst_34780);

return statearr_34827;
})();
if(cljs.core.truth_(inst_34781)){
var statearr_34828_34907 = state_34794__$1;
(statearr_34828_34907[(1)] = (44));

} else {
var statearr_34829_34908 = state_34794__$1;
(statearr_34829_34908[(1)] = (45));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (29))){
var inst_34711 = (state_34794[(23)]);
var inst_34716 = (state_34794[(24)]);
var inst_34748 = (state_34794[(16)]);
var inst_34712 = (state_34794[(25)]);
var inst_34708 = (state_34794[(19)]);
var inst_34714 = (state_34794[(26)]);
var inst_34744 = figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"debug","debug",-1608172596),"Figwheel: NOT loading these files ");
var inst_34747 = (function (){var all_files = inst_34708;
var res_SINGLEQUOTE_ = inst_34711;
var res = inst_34712;
var files_not_loaded = inst_34714;
var dependencies_that_loaded = inst_34716;
return ((function (all_files,res_SINGLEQUOTE_,res,files_not_loaded,dependencies_that_loaded,inst_34711,inst_34716,inst_34748,inst_34712,inst_34708,inst_34714,inst_34744,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents){
return (function (p__34746){
var map__34830 = p__34746;
var map__34830__$1 = ((((!((map__34830 == null)))?((((map__34830.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34830.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34830):map__34830);
var namespace = cljs.core.get.call(null,map__34830__$1,new cljs.core.Keyword(null,"namespace","namespace",-377510372));
var meta_data = cljs.core.get.call(null,cljs.core.deref.call(null,figwheel.client.file_reloading.figwheel_meta_pragmas),cljs.core.name.call(null,namespace));
if((meta_data == null)){
return new cljs.core.Keyword(null,"not-required","not-required",-950359114);
} else {
if(cljs.core.truth_(meta_data.call(null,new cljs.core.Keyword(null,"figwheel-no-load","figwheel-no-load",-555840179)))){
return new cljs.core.Keyword(null,"figwheel-no-load","figwheel-no-load",-555840179);
} else {
return new cljs.core.Keyword(null,"not-required","not-required",-950359114);

}
}
});
;})(all_files,res_SINGLEQUOTE_,res,files_not_loaded,dependencies_that_loaded,inst_34711,inst_34716,inst_34748,inst_34712,inst_34708,inst_34714,inst_34744,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents))
})();
var inst_34748__$1 = cljs.core.group_by.call(null,inst_34747,inst_34714);
var inst_34750 = (inst_34748__$1 == null);
var inst_34751 = cljs.core.not.call(null,inst_34750);
var state_34794__$1 = (function (){var statearr_34832 = state_34794;
(statearr_34832[(16)] = inst_34748__$1);

(statearr_34832[(28)] = inst_34744);

return statearr_34832;
})();
if(inst_34751){
var statearr_34833_34909 = state_34794__$1;
(statearr_34833_34909[(1)] = (32));

} else {
var statearr_34834_34910 = state_34794__$1;
(statearr_34834_34910[(1)] = (33));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (44))){
var inst_34770 = (state_34794[(21)]);
var inst_34783 = cljs.core.map.call(null,new cljs.core.Keyword(null,"file","file",-1269645878),inst_34770);
var inst_34784 = cljs.core.pr_str.call(null,inst_34783);
var inst_34785 = [cljs.core.str("not required: "),cljs.core.str(inst_34784)].join('');
var inst_34786 = figwheel.client.utils.log.call(null,inst_34785);
var state_34794__$1 = state_34794;
var statearr_34835_34911 = state_34794__$1;
(statearr_34835_34911[(2)] = inst_34786);

(statearr_34835_34911[(1)] = (46));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (6))){
var inst_34689 = (state_34794[(2)]);
var state_34794__$1 = state_34794;
var statearr_34836_34912 = state_34794__$1;
(statearr_34836_34912[(2)] = inst_34689);

(statearr_34836_34912[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (28))){
var inst_34714 = (state_34794[(26)]);
var inst_34741 = (state_34794[(2)]);
var inst_34742 = cljs.core.not_empty.call(null,inst_34714);
var state_34794__$1 = (function (){var statearr_34837 = state_34794;
(statearr_34837[(29)] = inst_34741);

return statearr_34837;
})();
if(cljs.core.truth_(inst_34742)){
var statearr_34838_34913 = state_34794__$1;
(statearr_34838_34913[(1)] = (29));

} else {
var statearr_34839_34914 = state_34794__$1;
(statearr_34839_34914[(1)] = (30));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (25))){
var inst_34712 = (state_34794[(25)]);
var inst_34728 = (state_34794[(2)]);
var inst_34729 = cljs.core.not_empty.call(null,inst_34712);
var state_34794__$1 = (function (){var statearr_34840 = state_34794;
(statearr_34840[(30)] = inst_34728);

return statearr_34840;
})();
if(cljs.core.truth_(inst_34729)){
var statearr_34841_34915 = state_34794__$1;
(statearr_34841_34915[(1)] = (26));

} else {
var statearr_34842_34916 = state_34794__$1;
(statearr_34842_34916[(1)] = (27));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (34))){
var inst_34763 = (state_34794[(2)]);
var state_34794__$1 = state_34794;
if(cljs.core.truth_(inst_34763)){
var statearr_34843_34917 = state_34794__$1;
(statearr_34843_34917[(1)] = (38));

} else {
var statearr_34844_34918 = state_34794__$1;
(statearr_34844_34918[(1)] = (39));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (17))){
var state_34794__$1 = state_34794;
var statearr_34845_34919 = state_34794__$1;
(statearr_34845_34919[(2)] = recompile_dependents);

(statearr_34845_34919[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (3))){
var state_34794__$1 = state_34794;
var statearr_34846_34920 = state_34794__$1;
(statearr_34846_34920[(2)] = null);

(statearr_34846_34920[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (12))){
var inst_34685 = (state_34794[(2)]);
var state_34794__$1 = state_34794;
var statearr_34847_34921 = state_34794__$1;
(statearr_34847_34921[(2)] = inst_34685);

(statearr_34847_34921[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (2))){
var inst_34647 = (state_34794[(13)]);
var inst_34654 = cljs.core.seq.call(null,inst_34647);
var inst_34655 = inst_34654;
var inst_34656 = null;
var inst_34657 = (0);
var inst_34658 = (0);
var state_34794__$1 = (function (){var statearr_34848 = state_34794;
(statearr_34848[(7)] = inst_34657);

(statearr_34848[(8)] = inst_34655);

(statearr_34848[(9)] = inst_34658);

(statearr_34848[(10)] = inst_34656);

return statearr_34848;
})();
var statearr_34849_34922 = state_34794__$1;
(statearr_34849_34922[(2)] = null);

(statearr_34849_34922[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (23))){
var inst_34711 = (state_34794[(23)]);
var inst_34716 = (state_34794[(24)]);
var inst_34712 = (state_34794[(25)]);
var inst_34708 = (state_34794[(19)]);
var inst_34714 = (state_34794[(26)]);
var inst_34719 = figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"debug","debug",-1608172596),"Figwheel: loaded these dependencies");
var inst_34721 = (function (){var all_files = inst_34708;
var res_SINGLEQUOTE_ = inst_34711;
var res = inst_34712;
var files_not_loaded = inst_34714;
var dependencies_that_loaded = inst_34716;
return ((function (all_files,res_SINGLEQUOTE_,res,files_not_loaded,dependencies_that_loaded,inst_34711,inst_34716,inst_34712,inst_34708,inst_34714,inst_34719,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents){
return (function (p__34720){
var map__34850 = p__34720;
var map__34850__$1 = ((((!((map__34850 == null)))?((((map__34850.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34850.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34850):map__34850);
var request_url = cljs.core.get.call(null,map__34850__$1,new cljs.core.Keyword(null,"request-url","request-url",2100346596));
return clojure.string.replace.call(null,request_url,goog.basePath,"");
});
;})(all_files,res_SINGLEQUOTE_,res,files_not_loaded,dependencies_that_loaded,inst_34711,inst_34716,inst_34712,inst_34708,inst_34714,inst_34719,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents))
})();
var inst_34722 = cljs.core.reverse.call(null,inst_34716);
var inst_34723 = cljs.core.map.call(null,inst_34721,inst_34722);
var inst_34724 = cljs.core.pr_str.call(null,inst_34723);
var inst_34725 = figwheel.client.utils.log.call(null,inst_34724);
var state_34794__$1 = (function (){var statearr_34852 = state_34794;
(statearr_34852[(31)] = inst_34719);

return statearr_34852;
})();
var statearr_34853_34923 = state_34794__$1;
(statearr_34853_34923[(2)] = inst_34725);

(statearr_34853_34923[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (35))){
var state_34794__$1 = state_34794;
var statearr_34854_34924 = state_34794__$1;
(statearr_34854_34924[(2)] = true);

(statearr_34854_34924[(1)] = (37));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (19))){
var inst_34698 = (state_34794[(12)]);
var inst_34704 = figwheel.client.file_reloading.expand_files.call(null,inst_34698);
var state_34794__$1 = state_34794;
var statearr_34855_34925 = state_34794__$1;
(statearr_34855_34925[(2)] = inst_34704);

(statearr_34855_34925[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (11))){
var state_34794__$1 = state_34794;
var statearr_34856_34926 = state_34794__$1;
(statearr_34856_34926[(2)] = null);

(statearr_34856_34926[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (9))){
var inst_34687 = (state_34794[(2)]);
var state_34794__$1 = state_34794;
var statearr_34857_34927 = state_34794__$1;
(statearr_34857_34927[(2)] = inst_34687);

(statearr_34857_34927[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (5))){
var inst_34657 = (state_34794[(7)]);
var inst_34658 = (state_34794[(9)]);
var inst_34660 = (inst_34658 < inst_34657);
var inst_34661 = inst_34660;
var state_34794__$1 = state_34794;
if(cljs.core.truth_(inst_34661)){
var statearr_34858_34928 = state_34794__$1;
(statearr_34858_34928[(1)] = (7));

} else {
var statearr_34859_34929 = state_34794__$1;
(statearr_34859_34929[(1)] = (8));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (14))){
var inst_34668 = (state_34794[(22)]);
var inst_34677 = cljs.core.first.call(null,inst_34668);
var inst_34678 = figwheel.client.file_reloading.eval_body.call(null,inst_34677,opts);
var inst_34679 = cljs.core.next.call(null,inst_34668);
var inst_34655 = inst_34679;
var inst_34656 = null;
var inst_34657 = (0);
var inst_34658 = (0);
var state_34794__$1 = (function (){var statearr_34860 = state_34794;
(statearr_34860[(7)] = inst_34657);

(statearr_34860[(8)] = inst_34655);

(statearr_34860[(9)] = inst_34658);

(statearr_34860[(32)] = inst_34678);

(statearr_34860[(10)] = inst_34656);

return statearr_34860;
})();
var statearr_34861_34930 = state_34794__$1;
(statearr_34861_34930[(2)] = null);

(statearr_34861_34930[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (45))){
var state_34794__$1 = state_34794;
var statearr_34862_34931 = state_34794__$1;
(statearr_34862_34931[(2)] = null);

(statearr_34862_34931[(1)] = (46));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (26))){
var inst_34711 = (state_34794[(23)]);
var inst_34716 = (state_34794[(24)]);
var inst_34712 = (state_34794[(25)]);
var inst_34708 = (state_34794[(19)]);
var inst_34714 = (state_34794[(26)]);
var inst_34731 = figwheel.client.utils.log.call(null,new cljs.core.Keyword(null,"debug","debug",-1608172596),"Figwheel: loaded these files");
var inst_34733 = (function (){var all_files = inst_34708;
var res_SINGLEQUOTE_ = inst_34711;
var res = inst_34712;
var files_not_loaded = inst_34714;
var dependencies_that_loaded = inst_34716;
return ((function (all_files,res_SINGLEQUOTE_,res,files_not_loaded,dependencies_that_loaded,inst_34711,inst_34716,inst_34712,inst_34708,inst_34714,inst_34731,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents){
return (function (p__34732){
var map__34863 = p__34732;
var map__34863__$1 = ((((!((map__34863 == null)))?((((map__34863.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34863.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34863):map__34863);
var namespace = cljs.core.get.call(null,map__34863__$1,new cljs.core.Keyword(null,"namespace","namespace",-377510372));
var file = cljs.core.get.call(null,map__34863__$1,new cljs.core.Keyword(null,"file","file",-1269645878));
if(cljs.core.truth_(namespace)){
return figwheel.client.file_reloading.name__GT_path.call(null,cljs.core.name.call(null,namespace));
} else {
return file;
}
});
;})(all_files,res_SINGLEQUOTE_,res,files_not_loaded,dependencies_that_loaded,inst_34711,inst_34716,inst_34712,inst_34708,inst_34714,inst_34731,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents))
})();
var inst_34734 = cljs.core.map.call(null,inst_34733,inst_34712);
var inst_34735 = cljs.core.pr_str.call(null,inst_34734);
var inst_34736 = figwheel.client.utils.log.call(null,inst_34735);
var inst_34737 = (function (){var all_files = inst_34708;
var res_SINGLEQUOTE_ = inst_34711;
var res = inst_34712;
var files_not_loaded = inst_34714;
var dependencies_that_loaded = inst_34716;
return ((function (all_files,res_SINGLEQUOTE_,res,files_not_loaded,dependencies_that_loaded,inst_34711,inst_34716,inst_34712,inst_34708,inst_34714,inst_34731,inst_34733,inst_34734,inst_34735,inst_34736,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents){
return (function (){
figwheel.client.file_reloading.on_jsload_custom_event.call(null,res);

return cljs.core.apply.call(null,on_jsload,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [res], null));
});
;})(all_files,res_SINGLEQUOTE_,res,files_not_loaded,dependencies_that_loaded,inst_34711,inst_34716,inst_34712,inst_34708,inst_34714,inst_34731,inst_34733,inst_34734,inst_34735,inst_34736,state_val_34795,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents))
})();
var inst_34738 = setTimeout(inst_34737,(10));
var state_34794__$1 = (function (){var statearr_34865 = state_34794;
(statearr_34865[(33)] = inst_34731);

(statearr_34865[(34)] = inst_34736);

return statearr_34865;
})();
var statearr_34866_34932 = state_34794__$1;
(statearr_34866_34932[(2)] = inst_34738);

(statearr_34866_34932[(1)] = (28));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (16))){
var state_34794__$1 = state_34794;
var statearr_34867_34933 = state_34794__$1;
(statearr_34867_34933[(2)] = reload_dependents);

(statearr_34867_34933[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (38))){
var inst_34748 = (state_34794[(16)]);
var inst_34765 = cljs.core.apply.call(null,cljs.core.hash_map,inst_34748);
var state_34794__$1 = state_34794;
var statearr_34868_34934 = state_34794__$1;
(statearr_34868_34934[(2)] = inst_34765);

(statearr_34868_34934[(1)] = (40));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (30))){
var state_34794__$1 = state_34794;
var statearr_34869_34935 = state_34794__$1;
(statearr_34869_34935[(2)] = null);

(statearr_34869_34935[(1)] = (31));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (10))){
var inst_34668 = (state_34794[(22)]);
var inst_34670 = cljs.core.chunked_seq_QMARK_.call(null,inst_34668);
var state_34794__$1 = state_34794;
if(inst_34670){
var statearr_34870_34936 = state_34794__$1;
(statearr_34870_34936[(1)] = (13));

} else {
var statearr_34871_34937 = state_34794__$1;
(statearr_34871_34937[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (18))){
var inst_34702 = (state_34794[(2)]);
var state_34794__$1 = state_34794;
if(cljs.core.truth_(inst_34702)){
var statearr_34872_34938 = state_34794__$1;
(statearr_34872_34938[(1)] = (19));

} else {
var statearr_34873_34939 = state_34794__$1;
(statearr_34873_34939[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (42))){
var state_34794__$1 = state_34794;
var statearr_34874_34940 = state_34794__$1;
(statearr_34874_34940[(2)] = null);

(statearr_34874_34940[(1)] = (43));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (37))){
var inst_34760 = (state_34794[(2)]);
var state_34794__$1 = state_34794;
var statearr_34875_34941 = state_34794__$1;
(statearr_34875_34941[(2)] = inst_34760);

(statearr_34875_34941[(1)] = (34));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34795 === (8))){
var inst_34668 = (state_34794[(22)]);
var inst_34655 = (state_34794[(8)]);
var inst_34668__$1 = cljs.core.seq.call(null,inst_34655);
var state_34794__$1 = (function (){var statearr_34876 = state_34794;
(statearr_34876[(22)] = inst_34668__$1);

return statearr_34876;
})();
if(inst_34668__$1){
var statearr_34877_34942 = state_34794__$1;
(statearr_34877_34942[(1)] = (10));

} else {
var statearr_34878_34943 = state_34794__$1;
(statearr_34878_34943[(1)] = (11));

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
}
});})(c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents))
;
return ((function (switch__29408__auto__,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents){
return (function() {
var figwheel$client$file_reloading$reload_js_files_$_state_machine__29409__auto__ = null;
var figwheel$client$file_reloading$reload_js_files_$_state_machine__29409__auto____0 = (function (){
var statearr_34882 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_34882[(0)] = figwheel$client$file_reloading$reload_js_files_$_state_machine__29409__auto__);

(statearr_34882[(1)] = (1));

return statearr_34882;
});
var figwheel$client$file_reloading$reload_js_files_$_state_machine__29409__auto____1 = (function (state_34794){
while(true){
var ret_value__29410__auto__ = (function (){try{while(true){
var result__29411__auto__ = switch__29408__auto__.call(null,state_34794);
if(cljs.core.keyword_identical_QMARK_.call(null,result__29411__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29411__auto__;
}
break;
}
}catch (e34883){if((e34883 instanceof Object)){
var ex__29412__auto__ = e34883;
var statearr_34884_34944 = state_34794;
(statearr_34884_34944[(5)] = ex__29412__auto__);


cljs.core.async.impl.ioc_helpers.process_exception.call(null,state_34794);

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
throw e34883;

}
}})();
if(cljs.core.keyword_identical_QMARK_.call(null,ret_value__29410__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34945 = state_34794;
state_34794 = G__34945;
continue;
} else {
return ret_value__29410__auto__;
}
break;
}
});
figwheel$client$file_reloading$reload_js_files_$_state_machine__29409__auto__ = function(state_34794){
switch(arguments.length){
case 0:
return figwheel$client$file_reloading$reload_js_files_$_state_machine__29409__auto____0.call(this);
case 1:
return figwheel$client$file_reloading$reload_js_files_$_state_machine__29409__auto____1.call(this,state_34794);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
figwheel$client$file_reloading$reload_js_files_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$0 = figwheel$client$file_reloading$reload_js_files_$_state_machine__29409__auto____0;
figwheel$client$file_reloading$reload_js_files_$_state_machine__29409__auto__.cljs$core$IFn$_invoke$arity$1 = figwheel$client$file_reloading$reload_js_files_$_state_machine__29409__auto____1;
return figwheel$client$file_reloading$reload_js_files_$_state_machine__29409__auto__;
})()
;})(switch__29408__auto__,c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents))
})();
var state__29522__auto__ = (function (){var statearr_34885 = f__29521__auto__.call(null);
(statearr_34885[cljs.core.async.impl.ioc_helpers.USER_START_IDX] = c__29520__auto__);

return statearr_34885;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped.call(null,state__29522__auto__);
});})(c__29520__auto__,map__34640,map__34640__$1,opts,before_jsload,on_jsload,reload_dependents,map__34641,map__34641__$1,msg,files,figwheel_meta,recompile_dependents))
);

return c__29520__auto__;
});
figwheel.client.file_reloading.current_links = (function figwheel$client$file_reloading$current_links(){
return Array.prototype.slice.call(document.getElementsByTagName("link"));
});
figwheel.client.file_reloading.truncate_url = (function figwheel$client$file_reloading$truncate_url(url){
return clojure.string.replace_first.call(null,clojure.string.replace_first.call(null,clojure.string.replace_first.call(null,clojure.string.replace_first.call(null,cljs.core.first.call(null,clojure.string.split.call(null,url,/\?/)),[cljs.core.str(location.protocol),cljs.core.str("//")].join(''),""),".*://",""),/^\/\//,""),/[^\\/]*/,"");
});
figwheel.client.file_reloading.matches_file_QMARK_ = (function figwheel$client$file_reloading$matches_file_QMARK_(p__34948,link){
var map__34951 = p__34948;
var map__34951__$1 = ((((!((map__34951 == null)))?((((map__34951.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34951.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34951):map__34951);
var file = cljs.core.get.call(null,map__34951__$1,new cljs.core.Keyword(null,"file","file",-1269645878));
var temp__4657__auto__ = link.href;
if(cljs.core.truth_(temp__4657__auto__)){
var link_href = temp__4657__auto__;
var match = clojure.string.join.call(null,"/",cljs.core.take_while.call(null,cljs.core.identity,cljs.core.map.call(null,((function (link_href,temp__4657__auto__,map__34951,map__34951__$1,file){
return (function (p1__34946_SHARP_,p2__34947_SHARP_){
if(cljs.core._EQ_.call(null,p1__34946_SHARP_,p2__34947_SHARP_)){
return p1__34946_SHARP_;
} else {
return false;
}
});})(link_href,temp__4657__auto__,map__34951,map__34951__$1,file))
,cljs.core.reverse.call(null,clojure.string.split.call(null,file,"/")),cljs.core.reverse.call(null,clojure.string.split.call(null,figwheel.client.file_reloading.truncate_url.call(null,link_href),"/")))));
var match_length = cljs.core.count.call(null,match);
var file_name_length = cljs.core.count.call(null,cljs.core.last.call(null,clojure.string.split.call(null,file,"/")));
if((match_length >= file_name_length)){
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"link","link",-1769163468),link,new cljs.core.Keyword(null,"link-href","link-href",-250644450),link_href,new cljs.core.Keyword(null,"match-length","match-length",1101537310),match_length,new cljs.core.Keyword(null,"current-url-length","current-url-length",380404083),cljs.core.count.call(null,figwheel.client.file_reloading.truncate_url.call(null,link_href))], null);
} else {
return null;
}
} else {
return null;
}
});
figwheel.client.file_reloading.get_correct_link = (function figwheel$client$file_reloading$get_correct_link(f_data){
var temp__4657__auto__ = cljs.core.first.call(null,cljs.core.sort_by.call(null,(function (p__34957){
var map__34958 = p__34957;
var map__34958__$1 = ((((!((map__34958 == null)))?((((map__34958.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34958.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34958):map__34958);
var match_length = cljs.core.get.call(null,map__34958__$1,new cljs.core.Keyword(null,"match-length","match-length",1101537310));
var current_url_length = cljs.core.get.call(null,map__34958__$1,new cljs.core.Keyword(null,"current-url-length","current-url-length",380404083));
return (current_url_length - match_length);
}),cljs.core.keep.call(null,(function (p1__34953_SHARP_){
return figwheel.client.file_reloading.matches_file_QMARK_.call(null,f_data,p1__34953_SHARP_);
}),figwheel.client.file_reloading.current_links.call(null))));
if(cljs.core.truth_(temp__4657__auto__)){
var res = temp__4657__auto__;
return new cljs.core.Keyword(null,"link","link",-1769163468).cljs$core$IFn$_invoke$arity$1(res);
} else {
return null;
}
});
figwheel.client.file_reloading.clone_link = (function figwheel$client$file_reloading$clone_link(link,url){
var clone = document.createElement("link");
clone.rel = "stylesheet";

clone.media = link.media;

clone.disabled = link.disabled;

clone.href = figwheel.client.file_reloading.add_cache_buster.call(null,url);

return clone;
});
figwheel.client.file_reloading.create_link = (function figwheel$client$file_reloading$create_link(url){
var link = document.createElement("link");
link.rel = "stylesheet";

link.href = figwheel.client.file_reloading.add_cache_buster.call(null,url);

return link;
});
figwheel.client.file_reloading.add_link_to_doc = (function figwheel$client$file_reloading$add_link_to_doc(var_args){
var args34960 = [];
var len__25865__auto___34963 = arguments.length;
var i__25866__auto___34964 = (0);
while(true){
if((i__25866__auto___34964 < len__25865__auto___34963)){
args34960.push((arguments[i__25866__auto___34964]));

var G__34965 = (i__25866__auto___34964 + (1));
i__25866__auto___34964 = G__34965;
continue;
} else {
}
break;
}

var G__34962 = args34960.length;
switch (G__34962) {
case 1:
return figwheel.client.file_reloading.add_link_to_doc.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return figwheel.client.file_reloading.add_link_to_doc.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args34960.length)].join('')));

}
});

figwheel.client.file_reloading.add_link_to_doc.cljs$core$IFn$_invoke$arity$1 = (function (new_link){
return (document.getElementsByTagName("head")[(0)]).appendChild(new_link);
});

figwheel.client.file_reloading.add_link_to_doc.cljs$core$IFn$_invoke$arity$2 = (function (orig_link,klone){
var parent = orig_link.parentNode;
if(cljs.core._EQ_.call(null,orig_link,parent.lastChild)){
parent.appendChild(klone);
} else {
parent.insertBefore(klone,orig_link.nextSibling);
}

return setTimeout(((function (parent){
return (function (){
return parent.removeChild(orig_link);
});})(parent))
,(300));
});

figwheel.client.file_reloading.add_link_to_doc.cljs$lang$maxFixedArity = 2;

figwheel.client.file_reloading.distictify = (function figwheel$client$file_reloading$distictify(key,seqq){
return cljs.core.vals.call(null,cljs.core.reduce.call(null,(function (p1__34967_SHARP_,p2__34968_SHARP_){
return cljs.core.assoc.call(null,p1__34967_SHARP_,cljs.core.get.call(null,p2__34968_SHARP_,key),p2__34968_SHARP_);
}),cljs.core.PersistentArrayMap.EMPTY,seqq));
});
figwheel.client.file_reloading.reload_css_file = (function figwheel$client$file_reloading$reload_css_file(p__34969){
var map__34972 = p__34969;
var map__34972__$1 = ((((!((map__34972 == null)))?((((map__34972.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34972.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34972):map__34972);
var f_data = map__34972__$1;
var file = cljs.core.get.call(null,map__34972__$1,new cljs.core.Keyword(null,"file","file",-1269645878));
var temp__4657__auto__ = figwheel.client.file_reloading.get_correct_link.call(null,f_data);
if(cljs.core.truth_(temp__4657__auto__)){
var link = temp__4657__auto__;
return figwheel.client.file_reloading.add_link_to_doc.call(null,link,figwheel.client.file_reloading.clone_link.call(null,link,link.href));
} else {
return null;
}
});
figwheel.client.file_reloading.reload_css_files = (function figwheel$client$file_reloading$reload_css_files(p__34974,p__34975){
var map__34984 = p__34974;
var map__34984__$1 = ((((!((map__34984 == null)))?((((map__34984.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34984.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34984):map__34984);
var opts = map__34984__$1;
var on_cssload = cljs.core.get.call(null,map__34984__$1,new cljs.core.Keyword(null,"on-cssload","on-cssload",1825432318));
var map__34985 = p__34975;
var map__34985__$1 = ((((!((map__34985 == null)))?((((map__34985.cljs$lang$protocol_mask$partition0$ & (64))) || (map__34985.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__34985):map__34985);
var files_msg = map__34985__$1;
var files = cljs.core.get.call(null,map__34985__$1,new cljs.core.Keyword(null,"files","files",-472457450));
if(cljs.core.truth_(figwheel.client.utils.html_env_QMARK_.call(null))){
var seq__34988_34992 = cljs.core.seq.call(null,figwheel.client.file_reloading.distictify.call(null,new cljs.core.Keyword(null,"file","file",-1269645878),files));
var chunk__34989_34993 = null;
var count__34990_34994 = (0);
var i__34991_34995 = (0);
while(true){
if((i__34991_34995 < count__34990_34994)){
var f_34996 = cljs.core._nth.call(null,chunk__34989_34993,i__34991_34995);
figwheel.client.file_reloading.reload_css_file.call(null,f_34996);

var G__34997 = seq__34988_34992;
var G__34998 = chunk__34989_34993;
var G__34999 = count__34990_34994;
var G__35000 = (i__34991_34995 + (1));
seq__34988_34992 = G__34997;
chunk__34989_34993 = G__34998;
count__34990_34994 = G__34999;
i__34991_34995 = G__35000;
continue;
} else {
var temp__4657__auto___35001 = cljs.core.seq.call(null,seq__34988_34992);
if(temp__4657__auto___35001){
var seq__34988_35002__$1 = temp__4657__auto___35001;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__34988_35002__$1)){
var c__25601__auto___35003 = cljs.core.chunk_first.call(null,seq__34988_35002__$1);
var G__35004 = cljs.core.chunk_rest.call(null,seq__34988_35002__$1);
var G__35005 = c__25601__auto___35003;
var G__35006 = cljs.core.count.call(null,c__25601__auto___35003);
var G__35007 = (0);
seq__34988_34992 = G__35004;
chunk__34989_34993 = G__35005;
count__34990_34994 = G__35006;
i__34991_34995 = G__35007;
continue;
} else {
var f_35008 = cljs.core.first.call(null,seq__34988_35002__$1);
figwheel.client.file_reloading.reload_css_file.call(null,f_35008);

var G__35009 = cljs.core.next.call(null,seq__34988_35002__$1);
var G__35010 = null;
var G__35011 = (0);
var G__35012 = (0);
seq__34988_34992 = G__35009;
chunk__34989_34993 = G__35010;
count__34990_34994 = G__35011;
i__34991_34995 = G__35012;
continue;
}
} else {
}
}
break;
}

return setTimeout(((function (map__34984,map__34984__$1,opts,on_cssload,map__34985,map__34985__$1,files_msg,files){
return (function (){
figwheel.client.file_reloading.on_cssload_custom_event.call(null,files);

return on_cssload.call(null,files);
});})(map__34984,map__34984__$1,opts,on_cssload,map__34985,map__34985__$1,files_msg,files))
,(100));
} else {
return null;
}
});

//# sourceMappingURL=file_reloading.js.map?rel=1485470173836