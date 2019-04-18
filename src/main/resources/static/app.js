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
                let seconds = (new Date() - startTime)/1000;

                 $("#progressInfo").empty().append("წამი: "+seconds);
               }
           });
        });


        $("#copylocal").submit(function(event) {
                var fada = a.connect;
                event.preventDefault();

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
                        let seconds = (new Date() - startTime)/1000;
                        $("#progressInfo1").empty().append("წამი: "+seconds);
                    }
                });
            });




});


