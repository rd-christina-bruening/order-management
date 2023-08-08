import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewChild} from '@angular/core';
import {OrderDto, OrderManagementService} from '../../order-management.service';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {OrderDetailDialogComponent} from './order-detail-dialog/order-detail-dialog.component';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-order-table',
  templateUrl: './order-table.component.html',
  styleUrls: ['./order-table.component.css']
})
export class OrderTableComponent implements OnChanges {
  @Input()
  orders: OrderDto[] = [];

  @Output()
  dataChanged = new EventEmitter();

  dataSource = new MatTableDataSource<OrderDto>([])
  displayedColumns: string[] = [
    'deliveryDate',
    'customerName',
    'reference',
    'info',
    'delete'
  ];
  @ViewChild(MatSort)
  sort!: MatSort;

  constructor(public dialog: MatDialog, private orderManagementService: OrderManagementService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.dataSource.data = this.orders;
    this.dataSource.sort = this.sort;
  }

  openOrderDetailsDialog(order: OrderDto) {
    const dialogRef = this.dialog.open(OrderDetailDialogComponent, {
      data: {order}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  onClickDelete(customerEmailAddress: string, deliveryDate: Date) {
    this.orderManagementService.deleteOrder(customerEmailAddress, deliveryDate).subscribe(_=>
    {
      this.dataChanged.emit();
    });
  }
}
