import { App } from './../../app';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
// 1. Importamos o nosso Service recém-criado
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {

  formularioLogin: FormGroup;

  // 2. Injetamos o AuthService no construtor junto com o FormBuilder
  constructor(
    private construtorDeFormulario: FormBuilder,
    private authService: AuthService
  ) {
    this.formularioLogin = this.construtorDeFormulario.group({
      login: ['', [Validators.required]],
      senha: ['', [Validators.required]]
    });
  }

  entrar() {
    if (this.formularioLogin.valid) {

      const dadosDoFormulario = this.formularioLogin.value;
      console.log("Enviando para o Java...", dadosDoFormulario);

      // 3. Chamamos o Service e ficamos "escutando" (subscribe) a resposta do Java
      this.authService.fazerLogin(dadosDoFormulario).subscribe({

        // Se o Java disser OK (Status 200)
        next: (resposta) => {
          console.log("Sucesso! Resposta da API:", resposta);
          alert("Login aprovado pelo Back-end!");
          // Nosso próximo passo será guardar o Token que chega aqui!
        },

        // Se o Java disser Erro (Credenciais inválidas, Status 403/401)
        error: (erro) => {
          console.error("Deu ruim na API:", erro);
          alert("Usuário ou senha incorretos!");
        }

      });

    }
  }
}
