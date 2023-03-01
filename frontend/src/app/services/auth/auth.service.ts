import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginResponse } from 'src/app/responses/login-response';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  login(
    email: string,
    password: string,
    url: string = 'http://localhost:8080/api/v1/auth/authenticate'
  ): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(url, {
      email,
      password,
    });
  }
}
