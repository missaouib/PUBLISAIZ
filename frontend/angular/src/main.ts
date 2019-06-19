import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();
}

Date.prototype.toISOString = function () {
  const tzo = -this.getTimezoneOffset(),
    dif = tzo >= 0 ? '+' : '-',
    pad = function (num) {
      const norm = Math.floor(Math.abs(num));
      return (norm < 10 ? '0' : '') + norm;
    };
  const res = this.getFullYear() +
    '-' + pad(this.getMonth() + 1) +
    '-' + pad(this.getDate()) +
    'T' + pad(this.getHours()) +
    ':' + pad(this.getMinutes()) +
    ':' + pad(this.getSeconds()).toString() +
    dif.toString() + pad(tzo / 60).toString() +
    ':' + pad(tzo % 60).toString();
    console.log(res);
    return res;
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));

