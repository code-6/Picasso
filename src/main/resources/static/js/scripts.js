/*!
    * Start Bootstrap - SB Admin v7.0.5 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2022 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
    // 
// Scripts
// 

window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
            document.body.classList.toggle('sb-sidenav-toggled');
        }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

});

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
