CREATE TABLE transacoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    remetente_id BIGINT,
    destinatario_id BIGINT,
    valor DECIMAL(19,2) NOT NULL,
    data_hora TIMESTAMP NOT NULL,

    --- Relacionammento com a tabela de usuario
    CONSTRAINT fk_transacoes_remetente FOREIGN KEY (remetente_id) REFERENCES usuarios(id),
    CONSTRAINT fk_transacoes_destinatario FOREIGN KEY (destinatario_id) REFERENCES usuarios(id)
);