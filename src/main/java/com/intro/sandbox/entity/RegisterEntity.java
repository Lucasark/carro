package com.intro.sandbox.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("register")
public class RegisterEntity {

    @MongoId
    private String id;

    private String titulo;

    private String preco;

    private String local;

    private String info;

    private String link;

    private String imgUrl;

    @Builder.Default
    private Boolean remover = Boolean.FALSE;

    @Builder.Default
    private Boolean favorito = Boolean.FALSE;

    private Boolean novo;

}

