import { homeRjs } from './ractiveTemplate.js'

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
