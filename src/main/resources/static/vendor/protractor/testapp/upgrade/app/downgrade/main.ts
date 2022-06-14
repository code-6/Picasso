import {downgradeModule} from '@angular/upgrade/static';
import {ng1module} from './ng1';
import {AppModuleNgFactory} from './ng2.ngfactory';

declare var angular: angular.IAngularStatic;

// Bootstrap Ng1 app as usual, but add a downgradedModule for the Angular (2+)
// part of the application.
angular.bootstrap(
    document.body, [ng1module.name, downgradeModule(AppModuleNgFactory)]);
