package com.sample.secondLevelCache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "items")
@EqualsAndHashCode(exclude = "items")
@Builder
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String owner;

    @Builder.Default
    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Item> items = new ArrayList<>();

    public boolean addItem(Item item) {
        item.setCart(this);
        return items.add(item);
    }

    public boolean deleteItem(Item item) {
        item.setCart(null);
        return items.remove(item);
    }
}
