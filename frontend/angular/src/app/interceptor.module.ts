
import { Injectable, NgModule } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Observable, throwError, pipe } from 'rxjs';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';

@Injectable()
export class HttpsRequestInterceptor implements HttpInterceptor {

    constructor(private router: Router) { }

    intercept(req: HttpRequest<any>, httpHandler: HttpHandler): Observable<HttpEvent<any>> {
        const dupReq = req.clone({
            withCredentials: true,
            headers: req.headers.set('X-Requested-With', 'XMLHttpRequest')
        });
        return httpHandler.handle(dupReq).pipe(
            catchError((err: HttpErrorResponse) => {
                if (err.status >= 400 && err.status < 500) {
                    this.router.navigate(['login']);
                } else {
                    return throwError(err);
                }
            })
        );
    }
}

@NgModule({
    providers: [
        { provide: HTTP_INTERCEPTORS, useClass: HttpsRequestInterceptor, multi: true }
    ]
})
export class InterceptorModule { }
