package DevBank.api_banco.service;

import DevBank.api_banco.dto.OperacaoDTO;
import DevBank.api_banco.model.Transacao;
import DevBank.api_banco.model.TransacaoRepository;
import DevBank.api_banco.model.Usuario;
import DevBank.api_banco.model.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OperacaoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Transactional
    public BigDecimal depositar(Long idUsuario, OperacaoDTO dto) {
        if (dto.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser maior que zero.");
        }

        Usuario usuario = usuarioRepository.findByIdComLock(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        // Adiciona o saldo
        usuario.setSaldo(usuario.getSaldo().add(dto.valor()));
        usuarioRepository.save(usuario);

        // Gera o recibo: No depósito, o remetente é nulo (o dinheiro vem de fora)
        Transacao deposito = new Transacao(null, usuario, dto.valor());
        transacaoRepository.save(deposito);

        // Retorna o saldo atualizado para o Controller
        return usuario.getSaldo();
    }

    @Transactional
    public BigDecimal sacar(Long idUsuario, OperacaoDTO dto) {
        if (dto.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser maior que zero.");
        }

        Usuario usuario = usuarioRepository.findByIdComLock(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        if (usuario.getSaldo().compareTo(dto.valor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para o saque.");
        }

        // Subtrai o saldo
        usuario.setSaldo(usuario.getSaldo().subtract(dto.valor()));
        usuarioRepository.save(usuario);

        // Gera o recibo: No saque, o destinatário é nulo (o dinheiro vai para fora)
        Transacao saque = new Transacao(usuario, null, dto.valor());
        transacaoRepository.save(saque);

        // Retorna o saldo atualizado para o Controller
        return usuario.getSaldo();
    }
}