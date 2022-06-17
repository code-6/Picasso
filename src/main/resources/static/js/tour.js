const tourUrl = 'http://localhost:8080/picasso/tour'
const ctxPath = '/picasso'

$('.filterBtn').on('click', function (data) {
    $.get(tourUrl, data);
});