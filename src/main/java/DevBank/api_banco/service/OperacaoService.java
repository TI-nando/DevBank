package DevBank.api_banco.service;

import DevBank.api_banco.dto.OperacaoDTO;
import DevBank.api_banco.dto.PixDTO;
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

        // LÓGICA DE LIMITE (CHEQUE ESPECIAL)

        BigDecimal limite = usuario.getLimiteCredito() != null ? usuario.getLimiteCredito() : BigDecimal.ZERO;
        BigDecimal saldoDisponivel = usuario.getSaldo().add(limite);

        if (saldoDisponivel.compareTo(dto.valor()) < 0) {
            throw new IllegalArgumentException("Saldo e limite insuficientes para o saque.");
        }

        // Subtrai o saldo (pode ficar negativo sem problemas!)
        usuario.setSaldo(usuario.getSaldo().subtract(dto.valor()));
        usuarioRepository.save(usuario);

        // Gera o recibo
        Transacao saque = new Transacao(usuario, null, dto.valor());
        transacaoRepository.save(saque);

        // Retorna o saldo atualizado para o Controller
        return usuario.getSaldo();
    }

    // MÉTODO: PIX

    @Transactional
    public BigDecimal realizarPix(Long idRemetente, PixDTO dto) {
        if (dto.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do Pix deve ser maior que zero.");
        }

        // 1. Busca quem está enviando com Lock de segurança
        Usuario remetente = usuarioRepository.findByIdComLock(idRemetente)
                .orElseThrow(() -> new EntityNotFoundException("Remetente não encontrado!"));

        // 2. Busca quem vai receber PELA CHAVE PIX
        Usuario destinatario = usuarioRepository.findByChavePix(dto.chaveDestino())
                .orElseThrow(() -> new EntityNotFoundException("Chave Pix destino não encontrada!"));

        // 3. Valida se não está transferindo para si mesmo
        if (remetente.getId().equals(destinatario.getId())) {
            throw new IllegalArgumentException("Você não pode fazer um Pix para a sua própria chave!");
        }

        // LÓGICA DE LIMITE (CHEQUE ESPECIAL)

        BigDecimal limite = remetente.getLimiteCredito() != null ? remetente.getLimiteCredito() : BigDecimal.ZERO;
        BigDecimal saldoDisponivel = remetente.getSaldo().add(limite);

        if (saldoDisponivel.compareTo(dto.valor()) < 0) {
            throw new IllegalArgumentException("Saldo e limite insuficientes para realizar o Pix!");
        }

        // 5. Atualiza os saldos (pode ficar negativo pro remetente)
        remetente.setSaldo(remetente.getSaldo().subtract(dto.valor()));
        destinatario.setSaldo(destinatario.getSaldo().add(dto.valor()));

        usuarioRepository.save(remetente);
        usuarioRepository.save(destinatario);

        // 6. Salva a transação
        Transacao transacaoPix = new Transacao(remetente, destinatario, dto.valor());
        transacaoRepository.save(transacaoPix);

        // 7. Retorna o saldo atualizado
        return remetente.getSaldo();
    }
}