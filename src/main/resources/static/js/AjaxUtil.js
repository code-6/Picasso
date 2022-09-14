
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

export function del(endpointUrl) {
    return exchange('delete', endpointUrl);
}

export function delSync(endpointUrl) {
    return exchangeSync('delete', endpointUrl);
}

export function exchange(httpRequestType, endpointUrl, payloadData) {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: httpRequestType,
            url: endpointUrl,
            data: payloadData,
            success: function (response) {
                console.debug('HTTP %s %s response %s' , httpRequestType , endpointUrl , response);
                resolve(response);
            },
            error: function (error) {
                console.error('HTTP %s %s error %s' , httpRequestType , endpointUrl , error);
                reject(error);
            }
        });
    });
}

export function exchangeSync(httpRequestType, endpointUrl, payloadData) {
    let payload;
    $.ajax({
        type: httpRequestType,
        url: endpointUrl,
        async: false,
        data: payloadData,
        success: function (response) {
            console.debug('HTTP %s (sync) %s response %s' , httpRequestType , endpointUrl , response);
            payload = response;
        },
        error: function (error) {
            console.error('HTTP %s (sync) %s error %s' , httpRequestType , endpointUrl , error);
            throw error;
        }
    });
    return payload;
}