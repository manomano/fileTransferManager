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
    startTime = new Date().getTime();
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

function showResponse(message) {
    let response = message.split("-");
    document.getElementById("filesToDownload").innerHTML = response[0];
    document.getElementById("filesDownloaded").innerHTML = response[1];

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
                                    showResponse(greeting.body);
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
                let seconds = (new Date().getTime() - startTime)/1000;

                 $("#progressInfo").empty().append("წამი: "+seconds);
               }
           });

        });


});