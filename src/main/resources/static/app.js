var stompClient = null;
var startTime = null;


function openForm(evt, action) {
  var i, tabcontent, tablinks;
  tabcontent = document.getElementsByClassName("tabcontent");
  for (i = 0; i < tabcontent.length; i++) {
    tabcontent[i].style.display = "none";
  }
  tablinks = document.getElementsByClassName("tablinks");
  for (i = 0; i < tablinks.length; i++) {
    tablinks[i].className = tablinks[i].className.replace(" active", "");
  }
  document.getElementById(action).style.display = "block";
  evt.currentTarget.className += " active";
}





function setConnected(connected) {
    startTime = new Date();
    $("#info_excel").show();


}


function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/download/copyingFromServer', function (greeting) {
            showResponse(JSON.parse(greeting.body).content);
        });
    });
}
/*
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}
*/

function showResponse(message, motherDivId) {
    let response = message.split("-");
    document.getElementById(motherDivId).innerHTML = "<span>გადმოსაწერი ფაილების ოდენობა: </span><span>"+response[0]+"</span><span> გადმოწერილი ფაილების ოდენობა: </span> <span>"+response[1]+"</span>"

    //$("#progressTable").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
   /* $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });*/
});


let a = {};
a.connect = connect;

$( document ).ready(function() {

function returnTimePassed(seconds){
    let theRestSeconds = seconds % 3600;
    let hours = (seconds - (theRestSeconds))/3600;

    let minutes = (theRestSeconds - (theRestSeconds%60))/60;
    seconds  = theRestSeconds%60;

    let answer = "";
    if(hours){
        answer +="საათი: " + hours;
    }

    if(minutes){
            answer +=" წუთი: " + minutes;
    }

    if(seconds){
        answer +=" წამი: " + seconds;
    }

    return answer;


}
        $("#excel").submit(function(event) {
            var fada = a.connect;
            event.preventDefault();

            var $form = $(this),
            url = $form.attr('action');
            $("#info_excel").show();

             var socket = new SockJS('/gs-guide-websocket');
                            stompClient = Stomp.over(socket);
                            stompClient.connect({}, function (frame) {
                                //setConnected(true);
                                console.log('Connected:gaeshvaaa ' + frame);
                                stompClient.subscribe('/download/copyingFromServer', function (greeting) {
                                    showResponse(greeting.body,"info_excel");
                                });

                                stompClient.send("/excel", {}, "dadada");


                            });


            var form = $('#excel')[0];
            let fm = new FormData(form);
             $.ajax({
               url: url,
               type: 'POST',
               data: fm,
               cache: false,
               contentType: false,
               enctype: 'multipart/form-data',
               processData: false,
               success: function (response) {
                let seconds = Math.floor((new Date().getTime() - startTime.getTime())/1000);
                 $("#progressInfo").empty().append(returnTimePassed(seconds));
               }
           });
        });


        $("#copylocal").submit(function(event) {
                var fada = a.connect;
                event.preventDefault();
                startTime = new Date();

                var $form = $(this),
                    url = $form.attr('action');
                $("#info_excel1").show();

                var socket = new SockJS('/gs-guide-websocket');
                stompClient = Stomp.over(socket);
                stompClient.connect({}, function (frame) {
                    //setConnected(true);
                    console.log('Connected:gaeshvaaa ' + frame);
                    stompClient.subscribe('/download/copyingFromServer', function (greeting) {
                        showResponse(greeting.body,"info_excel1");
                    });

                    stompClient.send("/localDownload", {}, "dadada");


                });

                var form = $('#copylocal')[0];
                let fm = new FormData(form);
                $.ajax({
                    url: url,
                    type: 'POST',
                    data: fm,
                    cache: false,
                    contentType: false,
                    enctype: 'multipart/form-data',
                    processData: false,
                    success: function (response) {
                        let seconds = Math.floor((new Date().getTime() - startTime.getTime())/1000);
                        $("#progressInfo1").empty().append(returnTimePassed(seconds));
                        $("#response_copylocal").empty().append(" <h2>ფაილები აკლია</h2><table class='table table-striped' ><thead><tr><th>ფოლდერი</th><th>ფაილი</th></tr></thead><tbody id='response_copylocal_tr'></tbody></table>");
                        let data = response;
                        for(let val in response){
                            if(response[val].length==0){
                                $("#response_copylocal_tr").append("<tr><td>"+val+"</td><td>0</td></tr>")
                            }else{
                                for(let a in response[val]){
                                   $("#response_copylocal_tr").append("<tr><td>"+val+"</td><td>"+response[val][a]+"</td></tr>")
                                }
                            }

                 $("#progressInfo").empty().append("წამი: "+seconds);
               }
           });

        });


});