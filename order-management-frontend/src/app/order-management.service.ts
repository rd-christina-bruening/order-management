import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class OrderManagementService {

  constructor(private httpClient: HttpClient) {
  }

  getAllOrders() {
    return this.httpClient.get<OrderDto[]>('http://localhost:8080/api/orders');
  }

  saveNewOrder(formData: FormData) {
    return this.httpClient.post<OrderInputDto[]>('http://localhost:8080/api/orders', formData);
  }

  downloadFiles(deliveryDate: Date, customerEmailAddress: string) {
    return this.httpClient.get('http://localhost:8080/api/orders/zip/' + customerEmailAddress + '/' + deliveryDate, {responseType: 'blob'})
  }
}


export class OrderDto {
  customerEmailAddress: string = "";
  customerName: string = "";
  reference: string = "";
  deliveryDate: Date = new Date();
  numberOfFilesSaved: number | null = null;

  constructor(customerEmailAddress: string, customerName: string, reference: string, deliveryDate: string) {
    this.customerEmailAddress = customerEmailAddress;
    this.customerName = customerName;
    this.reference = reference;
    this.deliveryDate = new Date(deliveryDate);
  }
}

export class OrderInputDto {
  customerEmailAddress: string = "";
  customerName: string = "";
  reference: string = "";
  deliveryDate: Date = new Date();

  constructor(customerEmailAddress: string, customerName: string, reference: string, deliveryDate: string) {
    this.customerEmailAddress = customerEmailAddress;
    this.customerName = customerName;
    this.reference = reference;
    this.deliveryDate = new Date(deliveryDate);
  }
}
