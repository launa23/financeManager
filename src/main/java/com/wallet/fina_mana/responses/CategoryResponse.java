package com.wallet.fina_mana.responses;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private long id;
    private String name;
    private String icon;
    private int type;
    private String categoryOf;
    private List<CategoryResponse> categoryChilds;
}
