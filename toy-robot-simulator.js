$(function(){
    $("#frm_details").on("submit", function(event) {
        event.preventDefault();
        const apiBaseUrl = "http://localhost:8080/api";
        const headers = {
            'Accept': "application/json",
            'Content-Type': "application/json"
        };
        const txt = $("#txt").val().toLowerCase();
        if (txt.indexOf('place') > -1) {
            const placeDetails = txt.replace('place', '')
            const placeDetailsArray = placeDetails.trim().split(',');
            if (placeDetailsArray.length == 3) {
                const payload = '{ "f": "' + placeDetailsArray[2].toUpperCase() + '", "x": "' + placeDetailsArray[0] + '", "y": "' + placeDetailsArray[1] + '" }';
                var postRequest = $.ajax({
                    url: apiBaseUrl + '/toy-robot/place',
                    headers: headers,
                    type: "POST",
                    data: payload,
                    success: function (result) {
                        $("#resultOutput").text('Successfully Submitted "' + txt.toUpperCase() + '" command').css("color", "green");
                    }
                });
                postRequest.error(function (e) {
                    handleError(e);
                });
            }
            else {
                $("#resultOutput").text('Please check your "PLACE" submission command is correct').css("color", "red");
            }
        }
        else if (txt.toLowerCase() === 'move' || txt.toLowerCase() === 'left' || txt.toLowerCase() === 'right') {
            var updateRequest = $.ajax({
                url: apiBaseUrl + '/toy-robot/' + txt.toLowerCase(),
                headers: headers,
                type: "PUT",
                success: function (result) {
                    $("#resultOutput").text('Successfully Submitted "' + txt.toUpperCase() + '" command').css("color", "green");
                }
            });
            updateRequest.error(function(e) { handleError(e); });
        }
        else if (txt.toLowerCase() === 'report') {
            var getRequest = $.ajax({
                url: "http://localhost:8080/api/toy-robot/" + txt.toLowerCase(),
                headers: headers,
                type: "GET",
                success: function (result) {
                    $("#resultOutput").text(result.message).css("color", "green");
                }
            });
            getRequest.error(function(e) { handleError(e); });
        }
        else if (txt.trim().length === 0) {
            $("#resultOutput").text('Please enter a valid command').css("color", "red");
        }
        else {
            $("#resultOutput").text('Please check your submission command is correct').css("color", "red");
        }
    });

    var handleError = function(e) {
        if (e && e.responseJSON && e.responseJSON.message) {
            $("#resultOutput").text(e.responseJSON.message).css("color", "red");
        }
        else {
            $("#resultOutput").text('Please check your submission command is correct').css("color", "red");
        }
    }
});