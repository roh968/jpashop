package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    // 카테고리에 여러 아이템이 있고, 아이템도 여러 카테고리에 속할 수 있다
    // 중간 테이블(category_item)이 필요함
    // Category가 이 관계의 주인(owner)
    //→ 실제 중간 테이블 insert/update는 여기서 이루어짐



    //이 카테고리의 부모는 누구인가?
    //DB에 parent_id 외래 키를 만든다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    //이 카테고리의 자식들은 누구인가?
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //즉, 자기 자신(Category)을 부모/자식으로 연결해서 계층 구조 만든 것.

    //==연관관계 메서드==//
    // 부모-자식 관계를 한번에 정확하게 연결해주는 메서드
    public void addChildCategory(Category child) {
        this.child.add(child);  //Java 객체 동기화
        child.setParent(this);  //DB 외래 키 설정
    }
}

// DB는 계층 구조를 나타낼 때 하나의 테이블을 사용하며,
// 외래 키(Foreign Key)를 통해 부모 레코드를 참조합니다.

// JPA는 DB의 셀프 조인(Self-Join)이라는 기술적 구조를 이용하여,
// 개발자가 Java 코드에서 마치 실제 트리 구조를 탐색하듯
// getParent()와 getChild()를 통해
// 부모/자식 간을 자유롭게 이동할 수 있도록 만들어 줍니다.

// 무한한 깊이의 트리 구조를 구현하려면,
// 모든 카테고리 데이터는 하나의 Category 테이블에 저장
// 각 카테고리는 자신이 누구의 하위 카테고리인지를 알아야 합니다.
// 이 역할을 parent_id 외래 키가 수행하며,
// 이 외래 키는 결국 같은 Category 테이블의 ID를 참조
// Category 엔티티의 두 필드가 이 셀프 조인 관계를 완성합니다.
