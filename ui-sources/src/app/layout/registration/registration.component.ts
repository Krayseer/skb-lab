import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  userForm!: FormGroup;

  constructor(private fb: FormBuilder,
              private http: HttpClient) {
  }

  ngOnInit() {
    this.userForm = this.fb.group({
      login: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', Validators.required],
      name: ['', Validators.required]
    });
  }

  onSubmit() {
    const body = JSON.stringify(this.userForm.value);
    this.http.post('account', body, { headers: {'Content-Type': 'application/json' }});
  }

}
