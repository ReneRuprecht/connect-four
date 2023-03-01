import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor() {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error) => {
        let errorMsg = '';
        if (error instanceof HttpErrorResponse) {
          if (error.error instanceof ErrorEvent) {
            console.log('Error event');
          } else {
            switch (error.status) {
              case 401:
                errorMsg = error.status.toString();
                break;
              case 403:
                errorMsg = 'Zugriff verweigert';
                break;
            }
          }
        } else {
          console.log('An error occured');
        }

        return throwError(() => errorMsg);
      })
    );
  }
}
