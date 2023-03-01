import { Component } from '@angular/core';
import { AuthService } from '../services/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService) {}

  login() {
    if (!this.username || !this.password) {
      this.errorMessage = 'Felder Unvollständig';
      return;
    }
    const self = this;
    this.errorMessage = '';
    this.authService.login(this.username, this.password).subscribe({
      next(value) {
        console.log(value.token);
      },
      error(err) {
        self.errorMessage = err;
      },
      complete() {},
    });
  }
}
