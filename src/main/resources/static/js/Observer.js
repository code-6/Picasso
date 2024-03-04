export function FragmentInitializer() {
    this.observers = [];
}

FragmentInitializer.prototype = {
    subscribe: function (fn) {
        this.observers.push(fn);
        return this;
    },

    unsubscribe: function (fn) {
        this.observers = this.observers.filter(
            function (item) {
                if (item !== fn) {
                    return item;
                }
            }
        );
    },

    initialize: function (o, thisObj) {
        console.debug('notify observers');
        let scope = thisObj || window;
        this.observers.forEach(function (item) {
            item.call(scope, o);
            console.debug('notify observer %s', item.name);
        });
    }
};

