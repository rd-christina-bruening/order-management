import {Component, Input, OnChanges, SimpleChanges, ViewChild} from '@angular/core';
import {OrderDto} from '../../order-management.service';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';

@Component({
  selector: 'app-order-table',
  templateUrl: './order-table.component.html',
  styleUrls: ['./order-table.component.css']
})
export class OrderTableComponent implements OnChanges {
  @Input()
  orders: OrderDto[] = [];

  dataSource = new MatTableDataSource<OrderDto>([])
  displayedColumns: string[] = [
    'deliveryDate',
    'customerName',
    'customerEmailAddress',
    'reference'
  ];
  @ViewChild(MatSort)
  sort!: MatSort;

  ngOnChanges(changes: SimpleChanges): void {
    this.dataSource.data = this.orders;
    this.dataSource.sort = this.sort;
  }
}
