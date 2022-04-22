package dbc.vemser.refoundapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "item")
public class ItemEntity {

    @Id
    @Column(name = "id_item")
    private Integer idItem;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "value")
    private Double value;

    @Column(name = "attachment")
    private String image;

    @Column(name = "id_refund", insertable = false, updatable = false)
    private Integer idRefund;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_refund", referencedColumnName = "id_refund")
    private RefundEntity refundEntity;
}
