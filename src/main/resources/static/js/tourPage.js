import * as ajaxUtil from './AjaxUtil.js';
import {FragmentInitializer} from "./Observer.js";

const mainUrl = $("meta[name='mainUrl']").attr("content");

const userLocale = $("meta[name='userLocale']").attr('content');

const dateRangePickerDateFormat = $("meta[name='dateRangePickerDateFormat']").attr('content');

const toursTableFragmentSubject = new FragmentInitializer()
    .subscribe(initEditTourButtons);

const tourFormFragmentSubject = new FragmentInitializer()
    .subscribe(initSingleDatePickers) // reinitialize tour form date pickers
    .subscribe(initTourFormButtons); // reinitialize tour form buttons

const tourFormFilesFragmentSubject = new FragmentInitializer()
    .subscribe(initTourFormButtons);

// page load initialization
moment.locale(userLocale); // to fix different locale issue in date-range picker

$.fn.dataTable.moment(dateRangePickerDateFormat, userLocale); // to fix sort by date columns

$('#toursTable').DataTable({
    "drawCallback": function( settings ) {
        console.debug('datatables draw callback fired');
        toursTableFragmentSubject.initialize(); // to fix edit/delete tour buttons when search in tours table
    },
    paging: true,
    searching: true,
    ordering: true,
    fixedHeader: true,
    columnDefs: [
        {
            targets: 4,
            render: function (data, type, row) {
                return data.length > 80 ?
                    data.substr(0, 80) + '…' :
                    data;
            }
        }
    ]
});
console.debug('dataTables initialized');

$('#submitFilterBtn').on('click', function (e) {
    e.preventDefault();
    let btn = $(this);

    let submitUrl = mainUrl + '/tour';

    let formData = $('#tourFilterForm').serialize();

    btn.prop('disabled', true);

    ajaxUtil.get(submitUrl, formData)
        .then((toursTableHTMLFragment) => {
            console.debug('initialize datatables after filter submit');
            let toursTable = $('#toursTable');
            toursTable.DataTable().destroy();
            toursTable.html(toursTableHTMLFragment);
            toursTable.DataTable({
                columnDefs: [{
                    targets: 4,
                    render: function (data, type, row) {
                        return data.length > 80 ?
                            data.substr(0, 80) + '…' :
                            data;
                    }
                }]
            }).draw();
            console.debug('tours table DOM structure changed. Notify observers');
            toursTableFragmentSubject.initialize();
        })
        .catch((error) => {
            console.error('unable to filter tours %s', error);
            Swal.fire({
                icon: 'error',
                title: 'unable to filter tours'
            });
        })
        .finally(btn.prop('disabled', false));
});
console.debug('tours filter submit button initialized');

initSingleDatePickers();
initTourFormButtons();
// initEditTourButtons();

function initEditTourButtons() {
    console.debug('initEditTourButtons initialize tours table buttons (edit/delete)');
    $('.tourEditBtn').on('click', function (e) {
        e.preventDefault();
        let btn = $(this);
        btn.prop('disabled', true);

        let tourId = btn.attr('data-tour-id');

        let url = mainUrl + '/tour/' + tourId;

        ajaxUtil.get(url)
            .then((tourFormHTML) => {
                $('#tourForm').html(tourFormHTML);
                console.debug('after get tour form');
                tourFormFragmentSubject.initialize()
                $('#tourFormContainer').collapse('show');
            })
            .catch((error) => {
                console.error('unable to load tour form HTML for tour %d %s', tourId, error);
                Swal.fire({
                    icon: 'error',
                    title: 'unable to open tour form'
                });
            })
            .finally(btn.prop('disabled', false));
    });
}

function initSingleDatePickers () {
    console.debug('initSingleDatePickers initialize tour form date pickers');
    $('.date-picker-single').daterangepicker({
        singleDatePicker: true,
        showDropdowns: true,
        timePicker: true,
        timePicker24Hour: true,
        timePickerIncrement: 5,
        autoApply: true,
        autoUpdateInput: true,
        locale: {
            format: dateRangePickerDateFormat,
        }
    });
}

function initTourFormButtons () {
    console.debug('initTourFormButtons initialize tour form buttons (delete files/clear form)');
    $('.tourDeleteFileBtn').on('click', function (e) {
        e.preventDefault();
        let btn = $(this);
        btn.prop('disabled', true);

        let fileName = $(btn).attr('data-tour-file-name');
        let url = mainUrl + '/tour/file/'  + fileName;
        let tourFormData = $('#tourForm').serialize();

        ajaxUtil.del(url, tourFormData)
            .then((tourFormFilesTableHtml) => {
                $('#uploadedFilesTable').html(tourFormFilesTableHtml);
                tourFormFilesFragmentSubject.initialize();
            }).catch((error) => {
                console.error('unable to delete file %s %s',fileName, error);
                Swal.fire({
                    icon: 'error',
                    title: 'unable to delete tour file'
                });
            }).finally(btn.prop('disabled', false));
    });

    $('#resetTourFormBtn').on('click', function (e) {
        e.preventDefault();
        let btn = $(this);

        $(btn).prop('disabled', true);

        let url = mainUrl + '/tour/-1';

        ajaxUtil.get(url)
            .then((tourFormHTML) => {
                $('#tourForm').html(tourFormHTML);
                console.debug('tour form DOM structure changed. Notify observers');
                tourFormFragmentSubject.initialize();
            })
            .catch((error) => {
                console.error('unable to clear and load tour form HTML %s', error);
                Swal.fire({
                    icon: 'error',
                    title: 'unable to clear tour form'
                });
            })
            .finally(btn.prop('disabled', false));
    });

    $(function () {
        $('[data-toggle="popover"]').popover();
    });
}

