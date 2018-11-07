import Vue from 'vue'
import VueResource from 'vue-resource'
import Render from '@vue/render.vue'

String.prototype.noAccent = function(){
  var accent = [
    /[\300-\306]/g, /[\340-\346]/g, // A, a
    /[\310-\313]/g, /[\350-\353]/g, // E, e
    /[\314-\317]/g, /[\354-\357]/g, // I, i
    /[\322-\330]/g, /[\362-\370]/g, // O, o
    /[\331-\334]/g, /[\371-\374]/g, // U, u
    /[\321]/g, /[\361]/g, // N, n
    /[\307]/g, /[\347]/g, // C, c
    /-/g
  ];
  var noaccent = ['A','a','E','e','I','i','O','o','U','u','N','n','C','c',' '];

  var str = this;
  for (var i = 0; i < accent.length; i++){
    str = str.replace(accent[i], noaccent[i]);
  }

  return str;
}

Vue.use(VueResource)

let vue = new Vue({
  el: 'render',
  render: h => h(Render)
})
