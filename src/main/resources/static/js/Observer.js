export function FragmentSubject() {
    this.observers = [];
}

FragmentSubject.prototype = {
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

    notifyObservers: function (o, thisObj) {
        let scope = thisObj || window;
        this.observers.forEach(function (item) {
            item.call(scope, o);
        });
    }
};

