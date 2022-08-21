
function getToursForGanttChart(filterFormData, serverEndpoint, async) {
    if(!serverEndpoint)
        serverEndpoint = 'http://localhost:8080/picasso/api/tour/bind/gantt';
    if(typeof async == "undefined")
        async = true;

    let tasks;
    $.ajax({
        url: serverEndpoint,
        type: "post",
        async: async,
        data: filterFormData,
        success: function (responseData) {
            console.log('loadToursForGanttChart success. Filter data = ' + filterFormData + ' Received data = ' + responseData);
            tasks = responseData;
        },
        error: function (responseData) {
            console.error('failed HTTP GET call %s %s', serverEndpoint, responseData);
            throw 'unable to get tours for gantt chart';
        }
    });
    return tasks;
}

function bindEmployee(tourBindFormData, serverEndpoint, async) {
    if(!serverEndpoint)
        serverEndpoint = 'http://localhost:8080/picasso/';
    if(typeof async == "undefined")
        async = true;

    let tourBindFormFragment;
    $.ajax({
        url: serverEndpoint,
        type: "post",
        async: async,
        data: tourBindFormData,
        success: function (responseData) {
            console.log('bindEmployee success.');
            tourBindFormFragment = responseData;
        },
        error: function (responseData) {
            console.error('failed HTTP GET call %s %s', serverEndpoint, responseData);
            throw 'unable to bindEmployee';
        }
    });
    return tourBindFormFragment;
}

function validateBind(tourId, employeeId, dateRange) {
    $.ajax({
        url: mainUrl + '/api/bind/validate/' + tourId + '/' + employeeId + '/' + dateRange,
        type: "get",
        success: function () {
            console.log("bind is valid");
        },
        error: function (validateErrorMessage) {
            console.error('validate bind failed ' + validateErrorMessage);
            Swal.fire({
                icon: 'error',
                title: validateErrorMessage.responseJSON.message
            });
        }
    });
}

function getTour(tourId, async) {
    if(typeof async == "undefined")
        async = true;
    let tour;
    $.ajax({
        url: mainUrl + '/api/tour/' + tourId ,
        type: "get",
        async: async,
        success: function (response) {
            tour = response;
        },
        error: function (error) {
            console.error('failed get tour by id ' + tourId);
        }
    });
    return tour;
}



