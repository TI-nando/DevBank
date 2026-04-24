package DevBank.api_banco.infra;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // Esta classe vai capturar erros de todos os Controllers
public class TratadorDeErros {

    // Este método deve ser chamado sempre que um RuntimeException acontecer
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroDTO> tratarErroDeRegraDeNegocio(RuntimeException ex) {
        // Devolve o erro 400 (Bad Request) e um JSON limpinho com a mensagem do erro
        return ResponseEntity.badRequest().body(new ErroDTO(ex.getMessage()));
    }

    // Criamos um DTO rápido (record) aqui mesmo só para formatar a resposta como JSON
    public record ErroDTO(String erro) {
    }
}