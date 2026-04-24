package DevBank.api_banco.model.controller;

import DevBank.api_banco.model.dto.TransferenciaDTO;
import DevBank.api_banco.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transferencias") // Endereço desta funcionalidade
public class TransferenciaController {

    @Autowired
    private TransferenciaService service; // Chamamos o service

    @PostMapping
    public String transferir(@RequestBody TransferenciaDTO dados) {
        // Recebemos o JSON do Postman e mandamos para o Service fazer a magia
        service.realizarTransferencia(dados);

        return "Transferência realizada com sucesso!";
    }
}