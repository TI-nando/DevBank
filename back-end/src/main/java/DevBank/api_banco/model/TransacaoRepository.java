package DevBank.api_banco.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("SELECT t FROM Transacao t WHERE t.remetente.id = :usuarioId OR t.destinatario.id = :usuarioId ORDER BY t.dataHora DESC")
    List<Transacao> buscarExtratoPorUsuario(@Param("usuarioId") Long usuarioId);
}