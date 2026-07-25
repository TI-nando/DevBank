package DevBank.api_banco.service;

import DevBank.api_banco.model.Transacao;
import DevBank.api_banco.model.TransacaoRepository;
import DevBank.api_banco.model.Usuario;
import DevBank.api_banco.model.UsuarioRepository;
import DevBank.api_banco.dto.TransferenciaDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    // A anotação @Transactional é vital em sistemas bancarios
    @Transactional
    public void realizarTransferencia(TransferenciaDTO dados) {

        // 1. Ir buscar o remetente à base de dados usando o ID
        Usuario remetente = repository.findByIdComLock(dados.idRemetente())
                .orElseThrow(() -> new RuntimeException("Remetente não encontrado!"));

        // 2. Ir buscar o destinatário à base de dados usando o ID
        Usuario destinatario = repository.findByIdComLock(dados.idDestinatario())
                .orElseThrow(() -> new RuntimeException("Destinatário não encontrado!"));

        // 3. Verificar se o remetente tem saldo suficiente
        if (remetente.getSaldo().compareTo(dados.valor()) < 0) {
            throw new RuntimeException("Saldo insuficiente para realizar a transferência.");
        }

        // 4. Atualiza os saldos
        remetente.setSaldo(remetente.getSaldo().subtract(dados.valor()));
        destinatario.setSaldo(destinatario.getSaldo().add(dados.valor()));

        // 5. Guardar as alterações na base de dados
        repository.save(remetente);
        repository.save(destinatario);

        Transacao novaTransacao = new Transacao(remetente, destinatario, dados.valor());
        transacaoRepository.save(novaTransacao);

    }
}
