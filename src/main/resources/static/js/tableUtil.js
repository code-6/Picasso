/**
 * initialize table select/deselect toggle button
 * */


let selectAllMessage;
let deselectAllMessage;

$('.table-select-deselect-all-btn').click(function (e) {

    e.stopImmediatePropagation();
    e.preventDefault();

    let checkboxes = $('.table-select-deselect-item-checkbox');

    if(checkboxes.is(':checked')) {
        checkboxes.prop("checked", false);

        $('.table-select-deselect-all-span').text('Select all');

        $('.table-delete-items-btn').hide();
    } else {
        checkboxes.prop("checked", true);

        $('.table-select-deselect-all-span').text('Deselect all');

        $('.table-delete-items-span').text('Delete ' + $('.table-select-deselect-item-checkbox:checked').length + ' items');

        $('.table-delete-items-btn').show();
    }

    $(this).find('[data-fa-i2svg]')
        .toggleClass('fa-regular fa-square')
        .toggleClass('fa-solid fa-square-check');
});

/**
 * initialize table checkbox behaviour
 * */
function initOnChangeTableCheckBoxes() {
    $('.table-select-deselect-item-checkbox').change(function(e) {
        e.stopImmediatePropagation();
        e.preventDefault();

        let size = $('.table-select-deselect-item-checkbox:checked').length;

        if(size < 1) {
            $('.table-delete-items-btn').hide();
        } else {
            $('.table-delete-items-span').text('Delete ' + size + ' items');
            $('.table-delete-items-btn').show();
        }
    });
}

initOnChangeTableCheckBoxes(); // init for page first load

