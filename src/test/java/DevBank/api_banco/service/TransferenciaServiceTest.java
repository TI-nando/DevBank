package DevBank.api_banco.service;

import DevBank.api_banco.model.Usuario;
import DevBank.api_banco.model.UsuarioRepository;
import DevBank.api_banco.dto.TransferenciaDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TransferenciaService transferenciaService;

    @Test
    @DisplayName("Deve realizar transferencia com sucesso quando houver saldo suficiente")
    void deveRealizarTransferenciaComSucesso() {
        // 1. ARRANGE (Preparação do cenário)
        Usuario pagador = new Usuario();
        pagador.setId(1L);
        pagador.setSaldo(new BigDecimal("100.00")); // Usuário tem 100 reais

        Usuario recebedor = new Usuario();
        recebedor.setId(2L);
        recebedor.setSaldo(new BigDecimal("50.00")); // Recebedor tem 50 reais

        // MUDANÇA AQUI: Criando o Record/DTO passando os 3 argumentos direto (PagadorId, RecebedorId, Valor)
        TransferenciaDTO dto = new TransferenciaDTO(1L, 2L, new BigDecimal("30.00"));

        // Simulando o comportamento do banco de dados (Mockito)
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(pagador));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(recebedor));

        // 2. ACT (Ação - Rodando o seu código)
        // Nota: Adapte o nome do método 'realizarTransferencia' se estiver diferente no seu Service
        assertDoesNotThrow(() -> transferenciaService.realizarTransferencia(dto));

        // 3. ASSERT (Verificação)
        assertEquals(new BigDecimal("70.00"), pagador.getSaldo()); // 100 - 30 = 70
        assertEquals(new BigDecimal("80.00"), recebedor.getSaldo()); // 50 + 30 = 80

        // Verifica se o método de salvar no banco foi chamado para os dois usuários
        verify(usuarioRepository, times(1)).save(pagador);
        verify(usuarioRepository, times(1)).save(recebedor);
    }

    @Test
    @DisplayName("Deve lancar excecao quando o pagador nao tiver saldo suficiente")
    void deveLancarExcecaoQuandoNaoHouverSaldo() {
        // 1. ARRANGE
        Usuario pagador = new Usuario();
        pagador.setId(1L);
        pagador.setSaldo(new BigDecimal("10.00")); // Usuário só tem 10 reais

        Usuario recebedor = new Usuario();
        recebedor.setId(2L);
        recebedor.setSaldo(new BigDecimal("50.00"));

        // MUDANÇA AQUI TAMBÉM:
        TransferenciaDTO dto = new TransferenciaDTO(1L, 2L, new BigDecimal("30.00"));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(pagador));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(recebedor));

        // 2 & 3. ACT e ASSERT (Espera-se que o código estoure um Erro/Exception)
        // O aviso amarelo sumiu porque removemos a variável que não estava sendo usada
        assertThrows(RuntimeException.class, () -> transferenciaService.realizarTransferencia(dto));

        // Verifica que o banco NUNCA foi chamado para salvar, já que a transação falhou
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}