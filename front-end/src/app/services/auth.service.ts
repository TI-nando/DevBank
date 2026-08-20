import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // O endereço exato onde o Java está rodando
  private apiUrl = 'http://localhost:8080/login';

  constructor(private http: HttpClient) { }

  // Função que envia os dados para o Java
  fazerLogin(dadosLogin: any) {
    return this.http.post(this.apiUrl, dadosLogin);
  }
}
