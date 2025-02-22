package com.company.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "charities")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Charity extends AbstractItem {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "ein", nullable = false)
  private String ein;

  @Column(name = "description")
  private String description;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "attributes", columnDefinition = "json")
  private Map<String, Object> attributes;

  @OneToMany(mappedBy = "charity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Donation> donations = new ArrayList<Donation>();

  public Charity(String name, String ein, Map<String, Object> attributes) {
    this.setId(null);
    this.name = name;
    this.ein = ein;
    this.attributes = attributes;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("Charity[name=" + name);
    sb.append(", ein=" + ein);
    sb.append(", attributes=" + attributes);
    sb.append("]");

    return sb.toString();
  }
}
