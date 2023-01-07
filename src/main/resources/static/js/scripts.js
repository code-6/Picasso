/*!
    * Start Bootstrap - SB Admin v7.0.5 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2022 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
    // 
// Scripts
// 

const mainUrl = $("meta[name='mainUrl']").attr("content");

window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            toggleSideBar();
        });
    }

});

function toggleSideBar() {
    document.body.classList.toggle('sb-sidenav-toggled');
    let b = document.body.classList.contains('sb-sidenav-toggled');
    localStorage.setItem('sb|sidebar-toggle', b);
    let url = mainUrl + '/sidenav/collapse/' + b;
    $.ajax({
        url: url,
        type: 'PUT',
        success: function(result) {
            if(b) {
                console.log('sidenav collapsed ');
            } else {
                console.log('sidenav displayed ');
            }
        }
    });
}

/**
 * @param element any HTML element eg. button
 * */
function toggleSpinner(element) {

    if($(element).is('span') && $(element).hasClass('spinner-border')) {
        if($(element).css('display') === 'none') {
            $(element).show();
        } else {
            $(element).hide();
        }
    } else {
        // if element has icon child replace icon with running spinner.
        $(element).children('svg.svg-inline--fa').each(function(){
            if($(this).css('display') === 'none') {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
        $(element).children('span.spinner-border').each(function(){
            if($(this).css('display') === 'none') {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }
}

const DEFAULT_TOAST_TIME_OUT = 3000;
$(function () {
    $('[data-toggle="popover"]').popover();
});
