
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

