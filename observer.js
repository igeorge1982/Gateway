// create an observer instance
var target = document.querySelector('#something');
console.log(target);
var observer = new WebKitMutationObserver(function(mutations) {
    mutations.forEach(function(mutation) {
    console.log("Success");
    //$('#log').text('input text changed: "' + target.text() + '"');
    //console.log(mutation, mutation.type);
                                                            });
                                          });
observer.observe(target, {
                 attributes: true,
                 childList: true,
                 characterData: true,
                 subtree: true
                 });
//observer.disconnect(); - to stop observing

// test case
setInterval(function(){
            document.querySelector('#something').innerHTML = Math.random();
            },1000);