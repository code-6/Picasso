
export function get(endpointUrl, urlFormEncodedData) {
    return exchange('get', endpointUrl, urlFormEncodedData);
}

export function getSync(endpointUrl, urlFormEncodedData) {
    return exchangeSync('get', endpointUrl, urlFormEncodedData);
}

export function post(endpointUrl, payloadData) {
    return exchange('post', endpointUrl, payloadData);
}

export function postSync(endpointUrl, payloadData) {
    return exchangeSync('post', endpointUrl, payloadData);
}

export function del(endpointUrl, payloadData) {
    return exchange('delete', endpointUrl, payloadData);
}

export function delSync(endpointUrl, payloadData) {
    return exchangeSync('delete', endpointUrl, payloadData);
}

export function exchange(httpRequestType, endpointUrl, payloadData) {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: httpRequestType,
            url: endpointUrl,
            data: payloadData,
            success: function (response) {
                console.debug('HTTP %s %s data %s response %s' , httpRequestType , endpointUrl, payloadData, response);
                resolve(response);
            },
            error: function (error) {
                console.error('HTTP %s %s data %s error %s' , httpRequestType , endpointUrl, payloadData, error);
                reject(error);
            }
        });
    });
}

export function exchangeSync(httpRequestType, endpointUrl, payloadData) {
    let payload;
    let failed = false;
    let exception;
    $.ajax({
        type: httpRequestType,
        url: endpointUrl,
        async: false,
        data: payloadData,
        success: function (response) {
            console.debug('HTTP %s (sync) %s data %s response %s' , httpRequestType , endpointUrl, payloadData, response);
            payload = response;
        },
        error: function (error) {
            console.error('HTTP %s (sync) %s data %s error %s' , httpRequestType , endpointUrl, payloadData, error);
            failed = true;
            exception = error;
            throw error;
        }
    });
    if(failed) throw exception;

    return payload;
}