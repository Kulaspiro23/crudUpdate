// this homeRjs should be in the common.js or ractiveTemplate.js file for easy access of template
let homeRjs = new Ractive({ 
    el: '#container',
    template: '#homeRactive'   
});

let init = () =>{
    let url = '/home/getUserLoginDetails';
    $.ajax({
        type : 'POST',
        data : null,
        url:url,
        success: function(res){

        let response = res.response;
        let userDetails = response.userDetails;
        homeRjs.set('userDetails',userDetails);
          
        },
        error: function(res){
            console.log(res);
        }
    });
}   

init();
