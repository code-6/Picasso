function toggleExpandCollapseRow(tr, datatable) {
    // let tr = dtControl.closest('tr');
    let parentRowId = tr.attr('id');
    let row = datatable.row(tr);

    if (row.child.isShown()) {
        // This row is already open - close it
        row.child.hide();
        tr.removeClass('shown');
    } else {
        // Open this row
        let childRow = $('.child-row[data-parent-id="'+parentRowId+'"]').clone();
        childRow.show();
        if(childRow.length > 1) {
            childRow = childRow[0];
        }
        row.child(childRow).show();
        tr.addClass('shown');
    }
}